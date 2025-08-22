-- Migration V4: Criar tabela de logs de transações
-- Data: 2024-01-XX

-- Tabela de logs de transações
CREATE TABLE IF NOT EXISTS transaction_logs (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    transaction_id UUID NOT NULL,
    status VARCHAR(20) NOT NULL,
    message TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (transaction_id) REFERENCES payment_transactions(id) ON DELETE CASCADE
);

-- Índices para melhor performance
CREATE INDEX IF NOT EXISTS idx_transaction_logs_transaction_id ON transaction_logs(transaction_id);
CREATE INDEX IF NOT EXISTS idx_transaction_logs_status ON transaction_logs(status);
CREATE INDEX IF NOT EXISTS idx_transaction_logs_created_at ON transaction_logs(created_at);
CREATE INDEX IF NOT EXISTS idx_transaction_logs_transaction_status ON transaction_logs(transaction_id, status);

-- Comentários para documentação
COMMENT ON TABLE transaction_logs IS 'Tabela de logs de auditoria das transações';
COMMENT ON COLUMN transaction_logs.id IS 'Identificador único do log';
COMMENT ON COLUMN transaction_logs.transaction_id IS 'ID da transação (FK para payment_transactions)';
COMMENT ON COLUMN transaction_logs.status IS 'Status registrado no log';
COMMENT ON COLUMN transaction_logs.message IS 'Mensagem descritiva do log';
COMMENT ON COLUMN transaction_logs.created_at IS 'Data de criação do log';

-- Trigger para limpar logs antigos (opcional - para controle de volume)
-- CREATE OR REPLACE FUNCTION cleanup_old_logs()
-- RETURNS void AS $$
-- BEGIN
--     DELETE FROM transaction_logs 
--     WHERE created_at < CURRENT_TIMESTAMP - INTERVAL '1 year';
-- END;
-- $$ LANGUAGE plpgsql;

-- Agendar limpeza automática (opcional)
-- SELECT cron.schedule('cleanup-old-logs', '0 2 * * 0', 'SELECT cleanup_old_logs();');
