-- Migration V2: Criar tabela de clientes
-- Data: 2024-01-XX

-- Tabela de clientes
CREATE TABLE IF NOT EXISTS customers (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    external_id VARCHAR(100) NOT NULL UNIQUE,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE,
    document VARCHAR(20) UNIQUE,
    phone VARCHAR(20),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Índices para melhor performance
CREATE INDEX IF NOT EXISTS idx_customers_external_id ON customers(external_id);
CREATE INDEX IF NOT EXISTS idx_customers_email ON customers(email);
CREATE INDEX IF NOT EXISTS idx_customers_document ON customers(document);
CREATE INDEX IF NOT EXISTS idx_customers_created_at ON customers(created_at);

-- Comentários para documentação
COMMENT ON TABLE customers IS 'Tabela de clientes do sistema';
COMMENT ON COLUMN customers.id IS 'Identificador único do cliente';
COMMENT ON COLUMN customers.external_id IS 'ID externo do cliente (referência do sistema cliente)';
COMMENT ON COLUMN customers.name IS 'Nome completo do cliente';
COMMENT ON COLUMN customers.email IS 'Email do cliente (único)';
COMMENT ON COLUMN customers.document IS 'CPF ou CNPJ do cliente (único)';
COMMENT ON COLUMN customers.phone IS 'Telefone do cliente';
COMMENT ON COLUMN customers.created_at IS 'Data de criação do registro';
COMMENT ON COLUMN customers.updated_at IS 'Data da última atualização';

-- Trigger para atualizar updated_at automaticamente
CREATE OR REPLACE FUNCTION update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ language 'plpgsql';

CREATE TRIGGER update_customers_updated_at 
    BEFORE UPDATE ON customers 
    FOR EACH ROW 
    EXECUTE FUNCTION update_updated_at_column();
