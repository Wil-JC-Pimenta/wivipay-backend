-- Tabela de transações de pagamento
CREATE TABLE payment_transactions (
    id UUID PRIMARY KEY,
    provider VARCHAR(50) NOT NULL,
    provider_transaction_id VARCHAR(100) NOT NULL,
    amount DECIMAL(19,4) NOT NULL,
    currency VARCHAR(3) NOT NULL,
    status VARCHAR(20) NOT NULL,
    payment_method VARCHAR(100) NOT NULL,
    raw_response TEXT,
    error_message TEXT,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    description VARCHAR(255),
    customer_id VARCHAR(100),
    metadata TEXT
);

-- Tabela de clientes
CREATE TABLE customers (
    id UUID PRIMARY KEY,
    external_id VARCHAR(100) NOT NULL,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100),
    document VARCHAR(20),
    phone VARCHAR(20),
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    UNIQUE (external_id)
);

-- Tabela de cartões de crédito
CREATE TABLE credit_cards (
    id UUID PRIMARY KEY,
    customer_id UUID NOT NULL,
    provider_card_id VARCHAR(100) NOT NULL,
    last_four_digits VARCHAR(4) NOT NULL,
    brand VARCHAR(20) NOT NULL,
    expiration_month INTEGER NOT NULL,
    expiration_year INTEGER NOT NULL,
    is_default BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    FOREIGN KEY (customer_id) REFERENCES customers(id),
    UNIQUE (provider_card_id)
);

-- Tabela de logs de transações
CREATE TABLE transaction_logs (
    id UUID PRIMARY KEY,
    transaction_id UUID NOT NULL,
    status VARCHAR(20) NOT NULL,
    message TEXT,
    created_at TIMESTAMP NOT NULL,
    FOREIGN KEY (transaction_id) REFERENCES payment_transactions(id)
);

-- Índices para melhor performance
CREATE INDEX idx_payment_transactions_provider ON payment_transactions(provider);
CREATE INDEX idx_payment_transactions_status ON payment_transactions(status);
CREATE INDEX idx_payment_transactions_customer_id ON payment_transactions(customer_id);
CREATE INDEX idx_customers_external_id ON customers(external_id);
CREATE INDEX idx_credit_cards_customer_id ON credit_cards(customer_id);
CREATE INDEX idx_transaction_logs_transaction_id ON transaction_logs(transaction_id); 