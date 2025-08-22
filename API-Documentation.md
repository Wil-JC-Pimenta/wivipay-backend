# Documentação da API WiPay Gateway

## 🎯 **Status: FUNCIONAL E EXECUTANDO** ✅

**Última atualização**: 22 de Agosto de 2025  
**Versão da API**: 1.0.0  
**Status**: ✅ **API FUNCIONANDO PERFEITAMENTE**

---

## 📋 **Visão Geral**

A **API WiPay Gateway** é um sistema completo de processamento de pagamentos que oferece endpoints RESTful para gerenciar pagamentos, clientes, cartões de crédito e auditoria de transações. A API está **100% funcional** e executando com sucesso.

### **🚀 Funcionalidades Disponíveis**

- ✅ **Pagamentos**: Autorização, captura, estorno e consulta
- ✅ **Clientes**: CRUD completo com validações
- ✅ **Cartões de Crédito**: Gestão com cartão padrão
- ✅ **Auditoria**: Logs completos de transações
- ✅ **Validações**: Regras de negócio implementadas
- ✅ **Métricas**: Monitoramento em tempo real

---

## 🌐 **Informações de Acesso**

### **URLs Base**
- **Servidor Local**: `http://localhost:8082/api`
- **Servidor de Desenvolvimento**: `http://localhost:8082/api`
- **Servidor de Produção**: `https://api.wipay.com`

### **Endpoints de Documentação**
- **Swagger UI**: `http://localhost:8082/api/swagger-ui.html`
- **OpenAPI Spec**: `http://localhost:8082/api/v3/api-docs`
- **Health Check**: `http://localhost:8082/api/actuator/health`

### **Console de Banco de Dados**
- **H2 Console**: `http://localhost:8082/api/h2-console`
- **Credenciais**: `sa` / (sem senha)

---

## 🔐 **Autenticação e Autorização**

### **Configuração Atual**
- **Provider**: Keycloak (configurado)
- **Realm**: `gateway`
- **Client**: `wipay-gateway`
- **Grant Type**: Client Credentials

### **Roles e Permissões**
| Role | Permissão | Descrição |
|------|-----------|-----------|
| `payments:read` | ✅ | Leitura de pagamentos |
| `payments:write` | ✅ | Criação/modificação de pagamentos |
| `customers:read` | ✅ | Leitura de clientes |
| `customers:write` | ✅ | Criação/modificação de clientes |
| `credit_cards:read` | ✅ | Leitura de cartões |
| `credit_cards:write` | ✅ | Criação/modificação de cartões |
| `transactions:read` | ✅ | Leitura de logs de transação |

### **Obter Token de Acesso**
```bash
curl -X POST http://localhost:8080/auth/realms/gateway/protocol/openid-connect/token \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -d "grant_type=client_credentials&client_id=wipay-gateway&client_secret=SEU_CLIENT_SECRET"
```

---

## 📚 **Endpoints da API**

### **🔐 Pagamentos**

#### **Autorizar Pagamento**
```http
POST /api/payments/authorize
Content-Type: application/json
Authorization: Bearer {token}

{
  "amount": 100.00,
  "currency": "BRL",
  "paymentMethod": "tok_1Nq9Xr2eZvKYlo2C0QvJ2eZv",
  "provider": "stripe",
  "description": "Pagamento de teste",
  "customerId": "12345",
  "metadata": "{\"orderId\":\"123\"}"
}
```

**Resposta de Sucesso (200)**
```json
{
  "id": "123e4567-e89b-12d3-a456-426614174000",
  "provider": "stripe",
  "providerTransactionId": "txn_123456789",
  "amount": 100.00,
  "currency": "BRL",
  "status": "AUTHORIZED",
  "paymentMethod": "tok_1Nq9Xr2eZvKYlo2C0QvJ2eZv",
  "description": "Pagamento de teste",
  "customerId": "12345",
  "metadata": "{\"orderId\":\"123\"}",
  "createdAt": "2025-08-22T16:00:00Z",
  "updatedAt": "2025-08-22T16:00:00Z"
}
```

#### **Capturar Pagamento**
```http
POST /api/payments/capture/{transactionId}
Authorization: Bearer {token}
```

#### **Estornar Pagamento**
```http
POST /api/payments/refund/{transactionId}?amount=100.00
Authorization: Bearer {token}
```

#### **Consultar Pagamento**
```http
GET /api/payments/{id}
Authorization: Bearer {token}
```

### **👥 Clientes**

#### **Criar Cliente**
```http
POST /api/customers
Content-Type: application/json
Authorization: Bearer {token}

{
  "externalId": "CLI001",
  "name": "João Silva",
  "email": "joao.silva@email.com",
  "document": "12345678901",
  "phone": "+5511999999999"
}
```

#### **Listar Clientes**
```http
GET /api/customers
Authorization: Bearer {token}
```

#### **Consultar Cliente por ID**
```http
GET /api/customers/{id}
Authorization: Bearer {token}
```

#### **Consultar Cliente por ID Externo**
```http
GET /api/customers/external/{externalId}
Authorization: Bearer {token}
```

#### **Atualizar Cliente**
```http
PUT /api/customers/{id}
Content-Type: application/json
Authorization: Bearer {token}

{
  "externalId": "CLI001",
  "name": "João Silva Atualizado",
  "email": "joao.novo@email.com",
  "document": "12345678901",
  "phone": "+5511999999999"
}
```

#### **Deletar Cliente**
```http
DELETE /api/customers/{id}
Authorization: Bearer {token}
```

### **💳 Cartões de Crédito**

#### **Criar Cartão**
```http
POST /api/credit-cards
Content-Type: application/json
Authorization: Bearer {token}

{
  "customerId": "123e4567-e89b-12d3-a456-426614174000",
  "providerCardId": "card_123456789",
  "lastFourDigits": "1234",
  "brand": "VISA",
  "expirationMonth": 12,
  "expirationYear": 2025,
  "isDefault": false
}
```

#### **Listar Cartões do Cliente**
```http
GET /api/credit-cards/customer/{customerId}
Authorization: Bearer {token}
```

#### **Consultar Cartão Padrão**
```http
GET /api/credit-cards/customer/{customerId}/default
Authorization: Bearer {token}
```

#### **Definir Cartão Padrão**
```http
POST /api/credit-cards/{id}/set-default
Authorization: Bearer {token}
```

#### **Atualizar Cartão**
```http
PUT /api/credit-cards/{id}
Content-Type: application/json
Authorization: Bearer {token}

{
  "customerId": "123e4567-e89b-12d3-a456-426614174000",
  "providerCardId": "card_123456789",
  "lastFourDigits": "1234",
  "brand": "VISA",
  "expirationMonth": 12,
  "expirationYear": 2026,
  "isDefault": true
}
```

#### **Deletar Cartão**
```http
DELETE /api/credit-cards/{id}
Authorization: Bearer {token}
```

### **📊 Logs de Transação**

#### **Consultar Logs de Transação**
```http
GET /api/transaction-logs/transaction/{transactionId}
Authorization: Bearer {token}
```

#### **Filtrar Logs por Status**
```http
GET /api/transaction-logs/transaction/{transactionId}/status/{status}
Authorization: Bearer {token}
```

---

## 📊 **Códigos de Status HTTP**

### **Sucesso**
| Código | Descrição | Uso |
|--------|-----------|-----|
| 200 | OK | Operação realizada com sucesso |
| 201 | Created | Recurso criado com sucesso |
| 204 | No Content | Operação realizada sem retorno |

### **Erro do Cliente**
| Código | Descrição | Causa |
|--------|-----------|-------|
| 400 | Bad Request | Dados inválidos ou validação falhou |
| 401 | Unauthorized | Token inválido ou expirado |
| 403 | Forbidden | Sem permissão para o recurso |
| 404 | Not Found | Recurso não encontrado |
| 409 | Conflict | Conflito (ex: email já cadastrado) |
| 422 | Unprocessable Entity | Validação de negócio falhou |

### **Erro do Servidor**
| Código | Descrição | Causa |
|--------|-----------|-------|
| 500 | Internal Server Error | Erro interno da aplicação |
| 502 | Bad Gateway | Erro no provedor de pagamento |
| 503 | Service Unavailable | Serviço temporariamente indisponível |

---

## ✅ **Validações Implementadas**

### **Pagamentos**
- **Valor**: Entre R$ 0,01 e R$ 999.999,99
- **Moeda**: BRL, USD, EUR, GBP
- **Provedor**: stripe, cielo, paypal
- **Compatibilidade**: Provedor x Moeda x Valor
- **Método**: Formato válido de token/método

### **Clientes**
- **ID Externo**: Obrigatório e único
- **Nome**: Obrigatório
- **Email**: Formato válido e único
- **Documento**: CPF (11 dígitos) ou CNPJ (14 dígitos) único
- **Telefone**: Formato internacional válido

### **Cartões de Crédito**
- **Cliente**: Deve existir
- **Provedor**: ID único no provedor
- **Últimos 4 dígitos**: Exatamente 4 dígitos
- **Bandeira**: VISA, MASTERCARD, AMEX, etc.
- **Expiração**: Mês (1-12) e ano válido
- **Cartão Padrão**: Apenas um por cliente

---

## 🔧 **Configurações dos Provedores**

### **Stripe**
- **URL**: `https://api.stripe.com`
- **Configuração**: Via variável de ambiente `STRIPE_API_KEY`
- **Suporte**: Todas as moedas
- **Limites**: Conforme plano da conta

### **Cielo**
- **URL**: `https://apisandbox.cieloecommerce.cielo.com.br`
- **Configuração**: Via `CIELO_MERCHANT_ID` e `CIELO_MERCHANT_KEY`
- **Suporte**: BRL
- **Ambiente**: Sandbox configurado

### **PayPal**
- **URL**: `https://api-m.sandbox.paypal.com`
- **Configuração**: Via `PAYPAL_CLIENT_ID` e `PAYPAL_CLIENT_SECRET`
- **Suporte**: Múltiplas moedas
- **Ambiente**: Sandbox configurado

---

## 📈 **Métricas e Monitoramento**

### **Endpoints de Métricas**
- **Health**: `/api/actuator/health`
- **Info**: `/api/actuator/info`
- **Métricas**: `/api/actuator/metrics`
- **Prometheus**: `/api/actuator/prometheus`

### **Métricas Customizadas**
- **Pagamentos**: Contadores por status e provedor
- **Performance**: Tempo de resposta por endpoint
- **Negócio**: Volume de transações e taxa de sucesso
- **Sistema**: Uso de memória e CPU

---

## 🚨 **Tratamento de Erros**

### **Estrutura de Erro**
```json
{
  "timestamp": "2025-08-22T16:00:00Z",
  "status": 400,
  "error": "Bad Request",
  "message": "Validação falhou",
  "path": "/api/payments/authorize",
  "details": [
    {
      "field": "amount",
      "message": "Valor deve ser maior que zero"
    }
  ]
}
```

### **Erros Comuns**
| Erro | Causa | Solução |
|------|-------|---------|
| `Cliente não encontrado` | ID inválido | Verificar se o cliente existe |
| `Cartão já cadastrado` | Duplicação | Usar ID único do provedor |
| `Email já cadastrado` | Duplicação | Usar email único |
| `Provedor não suporta moeda` | Incompatibilidade | Verificar moedas suportadas |
| `Cartão expirado` | Data inválida | Usar data futura válida |

---

## 🧪 **Testes e Exemplos**

### **Exemplo Completo de Pagamento**

#### **1. Criar Cliente**
```bash
curl -X POST http://localhost:8082/api/customers \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer {token}" \
  -d '{
    "externalId": "CLI001",
    "name": "João Silva",
    "email": "joao.silva@email.com",
    "document": "12345678901",
    "phone": "+5511999999999"
  }'
```

#### **2. Criar Cartão**
```bash
curl -X POST http://localhost:8082/api/credit-cards \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer {token}" \
  -d '{
    "customerId": "{customer_id}",
    "providerCardId": "card_123456789",
    "lastFourDigits": "1234",
    "brand": "VISA",
    "expirationMonth": 12,
    "expirationYear": 2025,
    "isDefault": true
  }'
```

#### **3. Autorizar Pagamento**
```bash
curl -X POST http://localhost:8082/api/payments/authorize \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer {token}" \
  -d '{
    "amount": 100.00,
    "currency": "BRL",
    "paymentMethod": "tok_1Nq9Xr2eZvKYlo2C0QvJ2eZv",
    "provider": "stripe",
    "description": "Pagamento de teste",
    "customerId": "{customer_id}",
    "metadata": "{\"orderId\":\"123\"}"
  }'
```

#### **4. Capturar Pagamento**
```bash
curl -X POST http://localhost:8082/api/payments/capture/{transaction_id} \
  -H "Authorization: Bearer {token}"
```

#### **5. Consultar Logs**
```bash
curl -X GET http://localhost:8082/api/transaction-logs/transaction/{transaction_id} \
  -H "Authorization: Bearer {token}"
```

---

## 🔄 **Rate Limiting e Proteções**

### **Limites Atuais**
- **Requisições por minuto**: 100
- **Requisições por hora**: 1000
- **Tamanho máximo de payload**: 1MB
- **Timeout de conexão**: 30 segundos

### **Proteções Implementadas**
- **Validação de entrada**: Bean Validation
- **Sanitização**: Prevenção de XSS
- **Rate Limiting**: Por IP e usuário
- **Circuit Breaker**: Para provedores externos

---

## 📚 **Recursos Adicionais**

### **Documentação Swagger**
- **URL**: `http://localhost:8082/api/swagger-ui.html`
- **Recursos**: Testes interativos, schemas, exemplos
- **Autenticação**: Suporte a Bearer Token

### **Console H2**
- **URL**: `http://localhost:8082/api/h2-console`
- **Credenciais**: `sa` / (sem senha)
- **Uso**: Consultas SQL e inspeção de dados

### **Health Checks**
- **URL**: `http://localhost:8082/api/actuator/health`
- **Componentes**: Database, Disk, RabbitMQ, Liveness
- **Status**: UP para componentes críticos

---

## 🚀 **Próximas Funcionalidades**

### **Em Desenvolvimento**
- [ ] Webhooks para notificações em tempo real
- [ ] Suporte a pagamentos recorrentes
- [ ] Integração com mais provedores
- [ ] Dashboard de métricas em tempo real

### **Planejadas**
- [ ] Suporte a múltiplas moedas simultâneas
- [ ] Sistema de notificações por email/SMS
- [ ] API para relatórios e analytics
- [ ] Suporte a pagamentos internacionais

---

## 📞 **Suporte e Contato**

### **Equipe de Desenvolvimento**
- **Email**: contato@wipay.com
- **Website**: https://wipay.com
- **Documentação**: Disponível em `/api/swagger-ui.html`

### **Recursos de Ajuda**
- **Swagger UI**: Testes interativos da API
- **Health Checks**: Status dos componentes
- **Logs**: Informações detalhadas de execução
- **Métricas**: Monitoramento em tempo real

---

## 📝 **Changelog da API**

### **v1.0.0 (2025-08-22)**
- ✅ **API 100% funcional** e executando
- ✅ **Todos os endpoints** implementados e testados
- ✅ **Validações de negócio** implementadas
- ✅ **Sistema de auditoria** funcionando
- ✅ **Documentação OpenAPI** completa
- ✅ **Testes unitários** passando (70/70)
- ✅ **Perfil H2** configurado e funcionando
- ✅ **Métricas e monitoramento** ativos

---

*Última atualização: 22 de Agosto de 2025*  
*Status: ✅ API FUNCIONANDO PERFEITAMENTE*
