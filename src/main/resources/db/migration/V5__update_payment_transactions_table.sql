-- Migration V5: Atualizar tabela de transações de pagamento
-- Data: 2024-01-XX

-- Adicionar novos campos à tabela payment_transactions
ALTER TABLE payment_transactions 
ADD COLUMN IF NOT EXISTS description VARCHAR(255),
ADD COLUMN IF NOT EXISTS customer_id VARCHAR(100),
ADD COLUMN IF NOT EXISTS metadata TEXT;

-- Adicionar índices para os novos campos
CREATE INDEX IF NOT EXISTS idx_payment_transactions_customer_id ON payment_transactions(customer_id);
CREATE INDEX IF NOT EXISTS idx_payment_transactions_description ON payment_transactions(description);

-- Comentários para os novos campos
COMMENT ON COLUMN payment_transactions.description IS 'Descrição do pagamento';
COMMENT ON COLUMN payment_transactions.customer_id IS 'ID do cliente (referência externa)';
COMMENT ON COLUMN payment_transactions.metadata IS 'Metadados adicionais do pagamento (JSON)';

-- Atualizar a migration V1 para incluir os novos campos
-- Esta migration garante que as instalações existentes tenham os novos campos
