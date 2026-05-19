-- Fact check status enum
CREATE TYPE fact_status AS ENUM ('PENDING', 'CHECKING', 'SUPPORTED', 'CONTRADICTED', 'INSUFFICIENT_EVIDENCE', 'NEEDS_REVIEW', 'MANUALLY_OVERRIDDEN');

-- Fact check jobs table
CREATE TABLE fact_check_jobs (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    session_id UUID NOT NULL REFERENCES conversation_sessions(id) ON DELETE CASCADE,
    status session_status NOT NULL DEFAULT 'QUEUED',
    started_at TIMESTAMP WITH TIME ZONE,
    completed_at TIMESTAMP WITH TIME ZONE,
    error_message TEXT,
    schema_version VARCHAR(10) DEFAULT '1.0',
    external_job_id VARCHAR(255),
    claims_extracted_count INTEGER DEFAULT 0,
    claims_verified_count INTEGER DEFAULT 0,
    correlation_id VARCHAR(255),
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    created_by UUID,
    updated_by UUID
);

-- Claims table
CREATE TABLE claims (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    session_id UUID NOT NULL REFERENCES conversation_sessions(id) ON DELETE CASCADE,
    segment_id UUID REFERENCES transcript_segments(id),
    claim_text TEXT NOT NULL,
    speaker_id UUID REFERENCES speakers(id),
    start_ms BIGINT,
    claim_type VARCHAR(100),
    status fact_status NOT NULL DEFAULT 'PENDING',
    confidence DECIMAL(5,4),
    verification_summary TEXT,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    created_by UUID,
    updated_by UUID
);

-- Evidence sources table
CREATE TABLE evidence_sources (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    claim_id UUID NOT NULL REFERENCES claims(id) ON DELETE CASCADE,
    source_type VARCHAR(100) NOT NULL,
    title VARCHAR(500),
    url TEXT,
    retrieved_at TIMESTAMP WITH TIME ZONE,
    snippet TEXT,
    reliability_score DECIMAL(5,4),
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    created_by UUID,
    updated_by UUID
);

-- Claim reviews table
CREATE TABLE claim_reviews (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    claim_id UUID NOT NULL REFERENCES claims(id) ON DELETE CASCADE,
    reviewer_user_id UUID NOT NULL REFERENCES users(id),
    old_status fact_status NOT NULL,
    new_status fact_status NOT NULL,
    review_note TEXT,
    reviewed_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    created_by UUID,
    updated_by UUID
);

-- Create indexes for fact check tables
CREATE INDEX idx_fact_check_jobs_session_id ON fact_check_jobs(session_id);
CREATE INDEX idx_fact_check_jobs_status ON fact_check_jobs(status);
CREATE INDEX idx_claims_session_id ON claims(session_id);
CREATE INDEX idx_claims_segment_id ON claims(segment_id);
CREATE INDEX idx_claims_status ON claims(status);
CREATE INDEX idx_evidence_sources_claim_id ON evidence_sources(claim_id);
CREATE INDEX idx_claim_reviews_claim_id ON claim_reviews(claim_id);