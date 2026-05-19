-- Analysis results table
CREATE TABLE analysis_results (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    session_id UUID NOT NULL REFERENCES conversation_sessions(id) ON DELETE CASCADE UNIQUE,
    schema_version VARCHAR(10) DEFAULT '1.0',
    overall_summary TEXT,
    short_summary TEXT,
    detailed_summary TEXT,
    key_points JSONB,
    sentiment_score DECIMAL(5,4),
    escalation_score DECIMAL(5,4),
    clarity_score DECIMAL(5,4),
    risk_score DECIMAL(5,4),
    engagement_score DECIMAL(5,4),
    status session_status NOT NULL DEFAULT 'PROCESSING',
    raw_analysis_payload JSONB,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    created_by UUID,
    updated_by UUID
);

-- Speaker insights table
CREATE TABLE speaker_insights (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    session_id UUID NOT NULL REFERENCES conversation_sessions(id) ON DELETE CASCADE,
    speaker_id UUID NOT NULL REFERENCES speakers(id) ON DELETE CASCADE,
    talk_time_ratio DECIMAL(5,4),
    interruption_count INTEGER DEFAULT 0,
    average_sentiment DECIMAL(5,4),
    dominant_topics JSONB,
    engagement_level DECIMAL(5,4),
    speaking_pace DECIMAL(7,2),
    question_count INTEGER DEFAULT 0,
    summary TEXT,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    created_by UUID,
    updated_by UUID,
    UNIQUE(session_id, speaker_id)
);

-- Insight flags table
CREATE TABLE insight_flags (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    session_id UUID NOT NULL REFERENCES conversation_sessions(id) ON DELETE CASCADE,
    flag_type VARCHAR(100) NOT NULL,
    severity INTEGER NOT NULL DEFAULT 3,
    description TEXT NOT NULL,
    evidence_segment_ids JSONB,
    confidence DECIMAL(5,4),
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    created_by UUID,
    updated_by UUID
);

-- Suggested action items table
CREATE TABLE suggested_action_items (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    session_id UUID NOT NULL REFERENCES conversation_sessions(id) ON DELETE CASCADE,
    action_type VARCHAR(100) NOT NULL,
    task_text TEXT NOT NULL,
    owner_suggestion VARCHAR(255),
    due_date_hint TIMESTAMP WITH TIME ZONE,
    confidence DECIMAL(5,4),
    status VARCHAR(50) DEFAULT 'SUGGESTED',
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    created_by UUID,
    updated_by UUID
);

-- Create indexes for insight tables
CREATE INDEX idx_analysis_results_session_id ON analysis_results(session_id);
CREATE INDEX idx_speaker_insights_session_id ON speaker_insights(session_id);
CREATE INDEX idx_speaker_insights_speaker_id ON speaker_insights(speaker_id);
CREATE INDEX idx_insight_flags_session_id ON insight_flags(session_id);
CREATE INDEX idx_insight_flags_type ON insight_flags(flag_type);
CREATE INDEX idx_suggested_action_items_session_id ON suggested_action_items(session_id);