#!/bin/bash

echo "🚀 Configurando PostgreSQL para o WiviPay Gateway..."

# Verificar se o Docker está rodando
if ! docker info > /dev/null 2>&1; then
    echo "❌ Docker não está rodando. Por favor, inicie o Docker primeiro."
    exit 1
fi

# Parar e remover containers existentes
echo "🔄 Parando containers existentes..."
docker-compose down

# Criar arquivo .env se não existir
if [ ! -f .env ]; then
    echo "📝 Criando arquivo .env..."
    cp env.example .env
    echo "✅ Arquivo .env criado. Edite as configurações se necessário."
fi

# Iniciar apenas o PostgreSQL
echo "🐘 Iniciando PostgreSQL..."
docker-compose up -d db

# Aguardar o PostgreSQL estar pronto
echo "⏳ Aguardando PostgreSQL estar pronto..."
sleep 10

# Verificar se o PostgreSQL está rodando
if docker-compose ps db | grep -q "Up"; then
    echo "✅ PostgreSQL está rodando!"
    echo "📊 Status dos containers:"
    docker-compose ps
    
    echo ""
    echo "🌐 Informações de conexão:"
    echo "   Host: localhost"
    echo "   Porta: 5432"
    echo "   Database: wivipay"
    echo "   Usuário: postgres"
    echo "   Senha: postgres123"
    echo ""
    echo "🔗 Para conectar via psql:"
    echo "   psql -h localhost -U postgres -d wivipay"
    echo ""
    echo "🚀 Para iniciar a aplicação com PostgreSQL:"
    echo "   mvn spring-boot:run"
    echo ""
    echo "📚 Para ver logs do PostgreSQL:"
    echo "   docker-compose logs db"
else
    echo "❌ Erro ao iniciar PostgreSQL. Verifique os logs:"
    docker-compose logs db
    exit 1
fi
