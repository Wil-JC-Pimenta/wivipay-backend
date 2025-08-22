# WiPay Gateway - Sistema de Pagamentos

## 🎯 **Status do Projeto: FUNCIONAL E EXECUTANDO** ✅

**Última atualização**: 22 de Agosto de 2025  
**Versão**: 1.0.0  
**Status**: ✅ **PROJETO EXECUTANDO COM SUCESSO**

---

## 📋 **Resumo Executivo**

O **WiPay Gateway** é um sistema completo de processamento de pagamentos que implementa uma arquitetura robusta com suporte a múltiplos provedores (Stripe, Cielo, PayPal), gerenciamento de clientes, cartões de crédito e auditoria completa de transações.

### 🚀 **Funcionalidades Implementadas e Testadas**

- ✅ **Sistema de Pagamentos**: Autorização, captura, estorno e consulta
- ✅ **Gestão de Clientes**: CRUD completo com validações de negócio
- ✅ **Gestão de Cartões**: CRUD com suporte a cartão padrão
- ✅ **Auditoria**: Logs completos de transações
- ✅ **Validações**: Regras de negócio complexas implementadas
- ✅ **Métricas**: Sistema de monitoramento com Micrometer
- ✅ **Segurança**: OAuth2 + JWT + Keycloak configurado
- ✅ **Documentação**: OpenAPI 3.0 completa
- ✅ **Testes**: 70 testes unitários executados com sucesso

---

## 🏗️ **Arquitetura**

### **Padrões Utilizados**
- **Clean Architecture** com separação clara de responsabilidades
- **Strategy Pattern** para provedores de pagamento
- **Repository Pattern** para acesso a dados
- **Service Layer** para lógica de negócio
- **DTO Pattern** para transferência de dados
- **Builder Pattern** para construção de objetos complexos

### **Tecnologias Core**
- **Backend**: Java 17 + Spring Boot 3.2.2
- **Database**: PostgreSQL (produção) + H2 (desenvolvimento)
- **ORM**: Spring Data JPA + Hibernate 6.4.1
- **Segurança**: Spring Security + OAuth2 + JWT
- **Documentação**: OpenAPI 3.0 + Swagger UI
- **Mensageria**: RabbitMQ (AMQP)
- **Monitoramento**: Spring Boot Actuator + Micrometer

---

## 🚀 **Status de Execução Atual**

### **✅ Aplicação Rodando**
- **Porta**: 8082
- **Context Path**: `/api`
- **Perfil Ativo**: `h2` (H2 em memória)
- **Status**: UP e funcionando

### **🌐 Endpoints Disponíveis**
- **API Base**: `http://localhost:8082/api`
- **Swagger UI**: `http://localhost:8082/api/swagger-ui.html`
- **OpenAPI Docs**: `http://localhost:8082/api/v3/api-docs`
- **Health Check**: `http://localhost:8082/api/actuator/health`
- **H2 Console**: `http://localhost:8082/api/h2-console`

### **📊 Status dos Componentes**
- **Database (H2)**: ✅ UP
- **Disk Space**: ✅ UP
- **Liveness/Ping**: ✅ UP
- **RabbitMQ**: ⚠️ DOWN (esperado, não configurado)
- **Status Geral**: ⚠️ DOWN (apenas por causa do RabbitMQ)

---

## 🛠️ **Configuração e Execução**

### **Pré-requisitos**
- Java 17 ou superior
- Maven 3.6+
- Docker (opcional, para testes de integração)

### **Execução Local**

#### **1. Perfil H2 (Recomendado para Desenvolvimento)**
```bash
mvn spring-boot:run -Dspring-boot.run.profiles=h2
```

#### **2. Perfil PostgreSQL (Produção)**
```bash
# Configurar PostgreSQL primeiro
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

#### **3. Perfil Padrão**
```bash
mvn spring-boot:run
```

### **Configurações Disponíveis**
- **`application.yml`**: Configuração padrão (PostgreSQL)
- **`application-h2.yml`**: Configuração H2 em memória
- **`application-dev.yml`**: Configuração de desenvolvimento

---

## 🧪 **Testes**

### **Status dos Testes**
- **Total de Testes**: 70
- **Testes de Serviço**: ✅ 70/70 PASSING
- **Testes de Integração**: ⚠️ 2/2 FAILING (Docker não disponível)

### **Execução de Testes**

#### **Todos os Testes (Exceto Integração)**
```bash
mvn test -DexcludedGroups="integration"
```

#### **Apenas Testes de Serviço**
```bash
mvn test -Dtest="*ServiceTest"
```

#### **Testes de Integração (Requer Docker)**
```bash
mvn test -Dgroups="integration"
```

---

## 📚 **Documentação da API**

### **Swagger UI**
Acesse `http://localhost:8082/api/swagger-ui.html` para:
- Visualizar todos os endpoints
- Testar as APIs interativamente
- Ver schemas e modelos
- Executar requisições de teste

### **OpenAPI Specification**
- **Endpoint**: `http://localhost:8082/api/v3/api-docs`
- **Formato**: JSON
- **Versão**: OpenAPI 3.0.1

### **Endpoints Principais**

#### **Pagamentos**
- `POST /api/payments/authorize` - Autorizar pagamento
- `POST /api/payments/capture/{id}` - Capturar pagamento
- `POST /api/payments/refund/{id}` - Estornar pagamento
- `GET /api/payments/{id}` - Consultar pagamento

#### **Clientes**
- `POST /api/customers` - Criar cliente
- `GET /api/customers` - Listar clientes
- `GET /api/customers/{id}` - Consultar cliente
- `PUT /api/customers/{id}` - Atualizar cliente
- `DELETE /api/customers/{id}` - Deletar cliente

#### **Cartões de Crédito**
- `POST /api/credit-cards` - Criar cartão
- `GET /api/credit-cards/customer/{customerId}` - Listar cartões do cliente
- `POST /api/credit-cards/{id}/set-default` - Definir cartão padrão

#### **Logs de Transação**
- `GET /api/transaction-logs/transaction/{transactionId}` - Consultar logs
- `GET /api/transaction-logs/transaction/{transactionId}/status/{status}` - Filtrar por status

---

## 🔐 **Segurança e Autenticação**

### **Configuração OAuth2**
- **Provider**: Keycloak
- **Realm**: `gateway`
- **Client**: `wipay-gateway`
- **Grant Types**: Client Credentials

### **Roles e Permissões**
- `payments:read` - Leitura de pagamentos
- `payments:write` - Criação/modificação de pagamentos
- `customers:read` - Leitura de clientes
- `customers:write` - Criação/modificação de clientes
- `credit_cards:read` - Leitura de cartões
- `credit_cards:write` - Criação/modificação de cartões
- `transactions:read` - Leitura de logs de transação

---

## 📊 **Monitoramento e Métricas**

### **Spring Boot Actuator**
- **Health Check**: `/api/actuator/health`
- **Info**: `/api/actuator/info`
- **Métricas**: `/api/actuator/metrics`
- **Prometheus**: `/api/actuator/prometheus`

### **Métricas Customizadas**
- Contadores de pagamentos por status
- Timers de resposta por endpoint
- Gauges de volume de transações
- Métricas de negócio (sucesso, falha, etc.)

---

## 🗄️ **Estrutura do Banco de Dados**

### **Tabelas Principais**
- `payment_transactions` - Transações de pagamento
- `customers` - Clientes
- `credit_cards` - Cartões de crédito
- `transaction_logs` - Logs de auditoria

### **Migrations Flyway**
- `V1__create_payment_transactions_table.sql`
- `V2__create_customers_table.sql`
- `V3__create_credit_cards_table.sql`
- `V4__create_transaction_logs_table.sql`
- `V5__update_payment_transactions_table.sql`

---

## 🚀 **Próximos Passos Recomendados**

### **Prioridade Alta**
1. **Configurar PostgreSQL** para ambiente de produção
2. **Configurar RabbitMQ** para mensageria
3. **Configurar Keycloak** para autenticação em produção

### **Prioridade Média**
4. **Implementar CI/CD** com GitHub Actions
5. **Configurar monitoramento** com Prometheus + Grafana
6. **Implementar rate limiting** e cache Redis

### **Prioridade Baixa**
7. **Adicionar testes de performance** com Gatling
8. **Implementar backup automático** do banco
9. **Configurar logs centralizados** com ELK Stack

---

## 🤝 **Contribuição**

### **Padrões de Commit (GitFlow)**
```
feat: nova funcionalidade
fix: correção de bug
docs: documentação
style: formatação de código
refactor: refatoração
test: testes
chore: tarefas de manutenção
```

### **Estrutura de Branches**
- `main` - Código de produção
- `develop` - Código de desenvolvimento
- `feature/*` - Novas funcionalidades
- `hotfix/*` - Correções urgentes
- `release/*` - Preparação de releases

---

## 📝 **Changelog**

### **v1.0.0 (2025-08-22)**
- ✅ Sistema de pagamentos completo implementado
- ✅ Gestão de clientes e cartões de crédito
- ✅ Sistema de auditoria e logs
- ✅ Validações de negócio implementadas
- ✅ Testes unitários (70 testes passando)
- ✅ Documentação OpenAPI completa
- ✅ Configuração de métricas e monitoramento
- ✅ Perfil H2 configurado e funcionando
- ✅ Aplicação executando com sucesso

---

## 📞 **Suporte**

- **Equipe**: WiPay Team
- **Email**: contato@wipay.com
- **Website**: https://wipay.com
- **Documentação**: Disponível em `/api/swagger-ui.html`

---

## 📄 **Licença**

Este projeto está licenciado sob a **MIT License** - veja o arquivo [LICENSE](LICENSE) para detalhes.

---

*Última atualização: 22 de Agosto de 2025*  
*Status: ✅ FUNCIONAL E EXECUTANDO*
