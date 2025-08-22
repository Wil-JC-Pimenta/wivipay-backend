#!/bin/bash

# Script para executar todos os testes do projeto WiPay Gateway
# Execute este script na raiz do projeto

echo "🧪 Executando Testes do WiPay Gateway..."
echo "=========================================="

# Verificar se o Maven está instalado
if ! command -v mvn &> /dev/null; then
    echo "❌ Maven não está instalado. Por favor, instale o Maven primeiro."
    exit 1
fi

# Verificar se estamos no diretório correto
if [ ! -f "pom.xml" ]; then
    echo "❌ Este script deve ser executado na raiz do projeto (onde está o pom.xml)"
    exit 1
fi

# Limpar e compilar o projeto
echo "🔨 Compilando o projeto..."
mvn clean compile

if [ $? -ne 0 ]; then
    echo "❌ Erro na compilação. Verifique os erros acima."
    exit 1
fi
echo "✅ Projeto compilado com sucesso!"

# Executar testes unitários
echo ""
echo "📋 Executando Testes Unitários..."
mvn test -Dtest="*Test" -DfailIfNoTests=false

if [ $? -ne 0 ]; then
    echo "❌ Alguns testes unitários falharam."
    UNIT_TESTS_FAILED=true
else
    echo "✅ Todos os testes unitários passaram!"
    UNIT_TESTS_FAILED=false
fi

# Executar testes de integração
echo ""
echo "🔗 Executando Testes de Integração..."
mvn test -Dtest="*IntegrationTest" -DfailIfNoTests=false

if [ $? -ne 0 ]; then
    echo "❌ Alguns testes de integração falharam."
    INTEGRATION_TESTS_FAILED=true
else
    echo "✅ Todos os testes de integração passaram!"
    INTEGRATION_TESTS_FAILED=false
fi

# Executar todos os testes
echo ""
echo "🚀 Executando Todos os Testes..."
mvn test -DfailIfNoTests=false

if [ $? -ne 0 ]; then
    echo "❌ Alguns testes falharam."
    ALL_TESTS_FAILED=true
else
    echo "✅ Todos os testes passaram!"
    ALL_TESTS_FAILED=false
fi

# Gerar relatório de cobertura
echo ""
echo "📊 Gerando Relatório de Cobertura..."
mvn jacoco:report

if [ $? -eq 0 ]; then
    echo "✅ Relatório de cobertura gerado!"
    echo "📁 Relatório disponível em: target/site/jacoco/index.html"
else
    echo "⚠️ Erro ao gerar relatório de cobertura."
fi

# Executar análise de qualidade com SonarQube (se configurado)
if [ -f "sonar-project.properties" ]; then
    echo ""
    echo "🔍 Executando Análise de Qualidade..."
    mvn sonar:sonar -Dsonar.host.url=http://localhost:9000
    
    if [ $? -eq 0 ]; then
        echo "✅ Análise de qualidade executada!"
        echo "📊 Acesse o SonarQube em: http://localhost:9000"
    else
        echo "⚠️ Erro na análise de qualidade."
    fi
fi

# Resumo final
echo ""
echo "=========================================="
echo "📋 RESUMO DOS TESTES"
echo "=========================================="

if [ "$UNIT_TESTS_FAILED" = true ]; then
    echo "❌ Testes Unitários: FALHARAM"
else
    echo "✅ Testes Unitários: PASSARAM"
fi

if [ "$INTEGRATION_TESTS_FAILED" = true ]; then
    echo "❌ Testes de Integração: FALHARAM"
else
    echo "✅ Testes de Integração: PASSARAM"
fi

if [ "$ALL_TESTS_FAILED" = true ]; then
    echo "❌ Todos os Testes: FALHARAM"
    echo ""
    echo "🔧 Para ver detalhes dos erros:"
    echo "   mvn test -Dtest=TestClassName#testMethodName"
    echo ""
    echo "📊 Para ver relatório detalhado:"
    echo "   mvn surefire-report:report"
    exit 1
else
    echo "✅ Todos os Testes: PASSARAM"
    echo ""
    echo "🎉 Parabéns! Todos os testes foram executados com sucesso!"
fi

echo ""
echo "📁 Arquivos gerados:"
echo "   - Relatórios de teste: target/surefire-reports/"
echo "   - Relatório de cobertura: target/site/jacoco/"
echo "   - JAR da aplicação: target/"

echo ""
echo "🚀 Para executar a aplicação:"
echo "   mvn spring-boot:run"

echo ""
echo "🔧 Para executar testes específicos:"
echo "   mvn test -Dtest=CustomerServiceTest"
echo "   mvn test -Dtest=CustomerServiceTest#shouldCreateCustomerSuccessfully"

echo ""
echo "📊 Para ver estatísticas dos testes:"
echo "   mvn surefire-report:report-only"
