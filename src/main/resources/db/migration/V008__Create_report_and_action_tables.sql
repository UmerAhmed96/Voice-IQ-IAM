-- Report type enum
CREATE TYPE report_type AS ENUM ('EXECUTIVE_SUMMARY', 'FULL_CONVERSATION', 'FACT_CHECKING', 'SPEAKER_ANALYSIS', 'CUSTOM');

-- Action type enum
CREATE TYPE action_type AS ENUM ('EMAIL', 'CALENDAR', 'TASK', 'NOTIFICATION', 'CRM_UPDATE', 'DOCUMENT', 'REMINDER', 'ESCALATION');

-- Action status enum
CREATE TYPE action_status AS ENUM ('SUGGESTED', 'APPROVED', 'EDITED', 'REJECTED', 'SCHEDULED', 'EXECUTING', 'COMPLETED', 'FAILED', 'CANCELLED');

-- Reports table
CREATE TABLE reports (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    session_id UUID NOT NULL REFERENCES conversation_sessions(id) ON DELETE CASCADE,
    report_type report_type NOT NULL,
    title VARCHAR(500) NOT NULL,
    status session_status NOT NULL DEFAULT 'QUEUED',
    file_format VARCHAR(10) DEFAULT 'PDF',
    storage_key VARCHAR(1000),
    file_size_bytes BIGINT,
    generated_at TIMESTAMP WITH TIME ZONE,
    expires_at TIMESTAMP WITH TIME ZONE,
    download_count INTEGER DEFAULT 0,
    error_message TEXT,
    template_version VARCHAR(10) DEFAULT '1.0',
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    created_by UUID,
    updated_by UUID
);

-- Suggested actions table (future agent actions)
CREATE TABLE suggested_actions (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    session_id UUID NOT NULL REFERENCES conversation_sessions(id) ON DELETE CASCADE,
    source_segment_id UUID REFERENCES transcript_segments(id),
    action_type action_type NOT NULL,
    title VARCHAR(500) NOT NULL,
    description TEXT,
    status action_status NOT NULL DEFAULT 'SUGGESTED',
    confidence DECIMAL(5,4),
    priority INTEGER DEFAULT 3,
    due_date_hint TIMESTAMP WITH TIME ZONE,
    action_payload JSONB,
    approved_at TIMESTAMP WITH TIME ZONE,
    rejected_at TIMESTAMP WITH TIME ZONE,
    executed_at TIMESTAMP WITH TIME ZONE,
    execution_result JSONB,
    rejection_reason TEXT,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    created_by UUID,
    updated_by UUID
);

-- Admin usage summary view (materialized for performance)
CREATE TABLE usage_summaries (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    organization_id UUID REFERENCES organizations(id),
    period_start DATE NOT NULL,
    period_end DATE NOT NULL,
    total_sessions INTEGER DEFAULT 0,
    total_processing_minutes INTEGER DEFAULT 0,
    total_storage_bytes BIGINT DEFAULT 0,
    failed_jobs_count INTEGER DEFAULT 0,
    reports_generated INTEGER DEFAULT 0,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    UNIQUE(organization_id, period_start, period_end)
);

-- Create indexes for reports and actions
CREATE INDEX idx_reports_session_id ON reports(session_id);
CREATE INDEX idx_reports_type ON reports(report_type);
CREATE INDEX idx_reports_status ON reports(status);
CREATE INDEX idx_suggested_actions_session_id ON suggested_actions(session_id);
CREATE INDEX idx_suggested_actions_status ON suggested_actions(status);
CREATE INDEX idx_suggested_actions_type ON suggested_actions(action_type);
CREATE INDEX idx_usage_summaries_org_period ON usage_summaries(organization_id, period_start, period_end);