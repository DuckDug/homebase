CREATE TABLE job_log
(
    id                BIGSERIAL PRIMARY KEY,                   -- auto-incrementing primary key
    job_name          VARCHAR(100) NOT NULL,                   -- name of job that is run
    started_at        TIMESTAMP    NOT NULL,                   -- time job started
    finished_at       TIMESTAMP,                               -- time job finished
    status            VARCHAR(20),                             -- status of job
    error_message     TEXT,                                    -- job error message
    records_processed INTEGER,                                 -- count of records processed
    created_at        TIMESTAMP    NOT NULL DEFAULT NOW()      -- time job was created at
);

COMMENT ON TABLE job_log IS 'Stores Critical Job Information';
COMMENT ON COLUMN job_log.job_name IS 'Stores name of the current job';
COMMENT ON COLUMN job_log.status IS 'Stores Job Status as such : SUCCESS/FAILED/PARTIAL';

CREATE INDEX idx_job_log_job_name ON job_log (job_name);
CREATE INDEX idx_job_log_status ON job_log (status);
CREATE INDEX idx_job_log_name_started_at ON job_log (job_name, started_at);