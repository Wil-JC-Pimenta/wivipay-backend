-- Migration V3: Criar tabela de cartões de crédito
-- Data: 2024-01-XX

-- Tabela de cartões de crédito
CREATE TABLE IF NOT EXISTS credit_cards (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    customer_id UUID NOT NULL,
    provider_card_id VARCHAR(100) NOT NULL UNIQUE,
    last_four_digits VARCHAR(4) NOT NULL,
    brand VARCHAR(20) NOT NULL,
    expiration_month INTEGER NOT NULL CHECK (expiration_month >= 1 AND expiration_month <= 12),
    expiration_year INTEGER NOT NULL CHECK (expiration_year >= 2024),
    is_default BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (customer_id) REFERENCES customers(id) ON DELETE CASCADE
);

-- Índices para melhor performance
CREATE INDEX IF NOT EXISTS idx_credit_cards_customer_id ON credit_cards(customer_id);
CREATE INDEX IF NOT EXISTS idx_credit_cards_provider_card_id ON credit_cards(provider_card_id);
CREATE INDEX IF NOT EXISTS idx_credit_cards_is_default ON credit_cards(is_default);
CREATE INDEX IF NOT EXISTS idx_credit_cards_brand ON credit_cards(brand);
CREATE INDEX IF NOT EXISTS idx_credit_cards_expiration ON credit_cards(expiration_year, expiration_month);

-- Comentários para documentação
COMMENT ON TABLE credit_cards IS 'Tabela de cartões de crédito dos clientes';
COMMENT ON COLUMN credit_cards.id IS 'Identificador único do cartão';
COMMENT ON COLUMN credit_cards.customer_id IS 'ID do cliente (FK para customers)';
COMMENT ON COLUMN credit_cards.provider_card_id IS 'ID do cartão no provedor de pagamento (único)';
COMMENT ON COLUMN credit_cards.last_four_digits IS 'Últimos 4 dígitos do cartão';
COMMENT ON COLUMN credit_cards.brand IS 'Bandeira do cartão (VISA, MASTERCARD, etc.)';
COMMENT ON COLUMN credit_cards.expiration_month IS 'Mês de expiração (1-12)';
COMMENT ON COLUMN credit_cards.expiration_year IS 'Ano de expiração (>= 2024)';
COMMENT ON COLUMN credit_cards.is_default IS 'Indica se é o cartão padrão do cliente';
COMMENT ON COLUMN credit_cards.created_at IS 'Data de criação do registro';
COMMENT ON COLUMN credit_cards.updated_at IS 'Data da última atualização';

-- Trigger para atualizar updated_at automaticamente
CREATE TRIGGER update_credit_cards_updated_at 
    BEFORE UPDATE ON credit_cards 
    FOR EACH ROW 
    EXECUTE FUNCTION update_updated_at_column();

-- Constraint para garantir que apenas um cartão seja padrão por cliente
CREATE UNIQUE INDEX IF NOT EXISTS idx_credit_cards_customer_default 
    ON credit_cards(customer_id) 
    WHERE is_default = TRUE;
