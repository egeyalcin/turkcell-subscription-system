CREATE TABLE outbox_messages (
    id BIGSERIAL PRIMARY KEY,
    aggregate_id VARCHAR(50) NOT NULL,
    payload TEXT NOT NULL,
    topic VARCHAR(100) NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'NEW',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    processed_at TIMESTAMP
);

CREATE INDEX idx_outbox_status ON outbox_messages(status);