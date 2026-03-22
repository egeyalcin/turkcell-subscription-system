CREATE TABLE payments (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    subscription_id UUID NOT NULL,
    user_id BIGINT NOT NULL,
    amount DECIMAL(19, 2),
    currency VARCHAR(3) DEFAULT 'TRY',
    status VARCHAR(50) NOT NULL,
    transaction_reference VARCHAR(100) UNIQUE,
    error_code VARCHAR(50),
    error_message VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL
);

CREATE INDEX idx_payments_subscription_id ON payments(subscription_id);
CREATE INDEX idx_payments_status ON payments(status);