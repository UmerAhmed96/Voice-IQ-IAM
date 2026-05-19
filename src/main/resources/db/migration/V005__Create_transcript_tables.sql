-- Speakers table
CREATE TABLE speakers (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    session_id UUID NOT NULL REFERENCES conversation_sessions(id) ON DELETE CASCADE,
    display_name VARCHAR(255),
    speaker_label VARCHAR(50) NOT NULL,
    total_talk_time_ms BIGINT DEFAULT 0,
    segment_count INTEGER DEFAULT 0,
    average_confidence DECIMAL(5,4),
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    created_by UUID,
    updated_by UUID
);

-- Transcript segments table
CREATE TABLE transcript_segments (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    session_id UUID NOT NULL REFERENCES conversation_sessions(id) ON DELETE CASCADE,
    speaker_id UUID NOT NULL REFERENCES speakers(id) ON DELETE CASCADE,
    start_ms BIGINT NOT NULL,
    end_ms BIGINT NOT NULL,
    text TEXT NOT NULL,
    confidence DECIMAL(5,4),
    language VARCHAR(10),
    sentiment_label VARCHAR(50),
    sentiment_score DECIMAL(5,4),
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    created_by UUID,
    updated_by UUID
);

-- Timeline markers table
CREATE TABLE timeline_markers (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    session_id UUID NOT NULL REFERENCES conversation_sessions(id) ON DELETE CASCADE,
    marker_type VARCHAR(50) NOT NULL,
    start_ms BIGINT NOT NULL,
    end_ms BIGINT,
    title VARCHAR(500) NOT NULL,
    description TEXT,
    severity INTEGER DEFAULT 3,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    created_by UUID,
    updated_by UUID
);

-- Create indexes for transcript tables
CREATE INDEX idx_speakers_session_id ON speakers(session_id);
CREATE INDEX idx_transcript_segments_session_id ON transcript_segments(session_id);
CREATE INDEX idx_transcript_segments_speaker_id ON transcript_segments(speaker_id);
CREATE INDEX idx_transcript_segments_time ON transcript_segments(start_ms, end_ms);
CREATE INDEX idx_timeline_markers_session_id ON timeline_markers(session_id);
CREATE INDEX idx_timeline_markers_time ON timeline_markers(start_ms);