-- Migration V1: Criar tabela base de transações de pagamento
-- Data: 2024-01-XX

-- Tabela de transações de pagamento
CREATE TABLE IF NOT EXISTS payment_transactions (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    provider VARCHAR(50) NOT NULL,
    provider_transaction_id VARCHAR(100) NOT NULL,
    amount DECIMAL(19,4) NOT NULL,
    currency VARCHAR(3) NOT NULL,
    status VARCHAR(20) NOT NULL,
    payment_method VARCHAR(100) NOT NULL,
    raw_response TEXT,
    error_message TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    description VARCHAR(255),
    customer_id VARCHAR(100),
    metadata TEXT
);

-- Índices para melhor performance
CREATE INDEX IF NOT EXISTS idx_payment_transactions_provider ON payment_transactions(provider);
CREATE INDEX IF NOT EXISTS idx_payment_transactions_status ON payment_transactions(status);
CREATE INDEX IF NOT EXISTS idx_payment_transactions_customer_id ON payment_transactions(customer_id);
CREATE INDEX IF NOT EXISTS idx_payment_transactions_created_at ON payment_transactions(created_at);

-- Comentários para documentação
COMMENT ON TABLE payment_transactions IS 'Tabela de transações de pagamento';
COMMENT ON COLUMN payment_transactions.id IS 'Identificador único da transação';
COMMENT ON COLUMN payment_transactions.provider IS 'Provedor de pagamento (stripe, cielo, paypal)';
COMMENT ON COLUMN payment_transactions.provider_transaction_id IS 'ID da transação no provedor';
COMMENT ON COLUMN payment_transactions.amount IS 'Valor da transação';
COMMENT ON COLUMN payment_transactions.currency IS 'Moeda da transação (BRL, USD, etc.)';
COMMENT ON COLUMN payment_transactions.status IS 'Status da transação (PENDING, AUTHORIZED, CAPTURED, REFUNDED, FAILED)';
COMMENT ON COLUMN payment_transactions.payment_method IS 'Método de pagamento utilizado';
COMMENT ON COLUMN payment_transactions.raw_response IS 'Resposta bruta do provedor';
COMMENT ON COLUMN payment_transactions.error_message IS 'Mensagem de erro (se houver)';
COMMENT ON COLUMN payment_transactions.description IS 'Descrição da transação';
COMMENT ON COLUMN payment_transactions.customer_id IS 'ID do cliente (referência externa)';
COMMENT ON COLUMN payment_transactions.metadata IS 'Metadados adicionais da transação';

-- Trigger para atualizar updated_at automaticamente
CREATE OR REPLACE FUNCTION update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ language 'plpgsql';

CREATE TRIGGER update_payment_transactions_updated_at 
    BEFORE UPDATE ON payment_transactions 
    FOR EACH ROW 
    EXECUTE FUNCTION update_updated_at_column(); 