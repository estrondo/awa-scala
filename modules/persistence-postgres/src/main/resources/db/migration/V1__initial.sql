CREATE SCHEMA IF NOT EXISTS awa;

CREATE TABLE IF NOT EXISTS awa.track (
    id VARCHAR(32),
    account_id VARCHAR(16),
    device_id VARCHAR(16),
    device_type VARCHAR(16),
    started_at TIMESTAMP WITH TIME ZONE,
    created_at TIMESTAMP WITH TIME ZONE,
    PRIMARY KEY (id)
);

