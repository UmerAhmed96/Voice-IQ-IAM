-- Session status enum
CREATE TYPE session_status AS ENUM ('DRAFT', 'AUDIO_UPLOADED', 'QUEUED', 'PROCESSING', 'COMPLETED', 'FAILED', 'ARCHIVED');

-- Source type enum  
CREATE TYPE source_type AS ENUM ('AUDIO_UPLOAD', 'LIVE_RECORDING', 'API_INTEGRATION');

-- Conversation sessions table
CREATE TABLE conversation_sessions (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    user_id UUID NOT NULL REFERENCES users(id),
    organization_id UUID REFERENCES organizations(id),
    team_id UUID REFERENCES teams(id),
    title VARCHAR(500) NOT NULL,
    description TEXT,
    source_type source_type NOT NULL DEFAULT 'AUDIO_UPLOAD',
    language_code VARCHAR(10) DEFAULT 'en',
    expected_speakers INTEGER,
    status session_status NOT NULL DEFAULT 'DRAFT',
    is_archived BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    created_by UUID REFERENCES users(id),
    updated_by UUID REFERENCES users(id)
);

-- Audio files table
CREATE TABLE audio_files (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    session_id UUID NOT NULL REFERENCES conversation_sessions(id) ON DELETE CASCADE,
    original_filename VARCHAR(500),
    content_type VARCHAR(100),
    file_size_bytes BIGINT,
    duration_seconds INTEGER,
    storage_key VARCHAR(1000) NOT NULL,
    checksum VARCHAR(255),
    upload_completed_at TIMESTAMP WITH TIME ZONE,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    created_by UUID REFERENCES users(id),
    updated_by UUID REFERENCES users(id),
    UNIQUE(session_id)
);

-- Processing jobs table
CREATE TABLE processing_jobs (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    session_id UUID NOT NULL REFERENCES conversation_sessions(id) ON DELETE CASCADE,
    job_type VARCHAR(50) NOT NULL DEFAULT 'FULL_PROCESSING',
    status session_status NOT NULL DEFAULT 'QUEUED',
    started_at TIMESTAMP WITH TIME ZONE,
    completed_at TIMESTAMP WITH TIME ZONE,
    failed_at TIMESTAMP WITH TIME ZONE,
    error_message TEXT,
    progress_percentage INTEGER DEFAULT 0,
    external_job_id VARCHAR(255),
    retry_count INTEGER DEFAULT 0,
    max_retries INTEGER DEFAULT 3,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    created_by UUID REFERENCES users(id),
    updated_by UUID REFERENCES users(id)
);

-- Audit logs table for important actions
CREATE TABLE audit_logs (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    user_id UUID REFERENCES users(id),
    organization_id UUID REFERENCES organizations(id),
    resource_type VARCHAR(100) NOT NULL,
    resource_id UUID NOT NULL,
    action VARCHAR(100) NOT NULL,
    old_values JSONB,
    new_values JSONB,
    ip_address INET,
    user_agent TEXT,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW()
);

-- Create indexes for sessions and related tables
CREATE INDEX idx_conversation_sessions_user_id ON conversation_sessions(user_id);
CREATE INDEX idx_conversation_sessions_org_id ON conversation_sessions(organization_id);
CREATE INDEX idx_conversation_sessions_team_id ON conversation_sessions(team_id);
CREATE INDEX idx_conversation_sessions_status ON conversation_sessions(status);
CREATE INDEX idx_conversation_sessions_created_at ON conversation_sessions(created_at);

CREATE INDEX idx_audio_files_session_id ON audio_files(session_id);
CREATE INDEX idx_audio_files_storage_key ON audio_files(storage_key);

CREATE INDEX idx_processing_jobs_session_id ON processing_jobs(session_id);
CREATE INDEX idx_processing_jobs_status ON processing_jobs(status);
CREATE INDEX idx_processing_jobs_external_job_id ON processing_jobs(external_job_id);

CREATE INDEX idx_audit_logs_user_id ON audit_logs(user_id);
CREATE INDEX idx_audit_logs_resource ON audit_logs(resource_type, resource_id);
CREATE INDEX idx_audit_logs_created_at ON audit_logs(created_at);