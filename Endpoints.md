# Documentação da API de Pagamentos

## Informações Gerais

- **URL Base**: `http://localhost:8080/api`
- **Autenticação**: Bearer Token (OAuth2)
- **Content-Type**: `application/json`

## Configuração do Insomnia

1. Crie um novo ambiente no Insomnia
2. Adicione as seguintes variáveis:

   - `base_url`: `http://localhost:8080/api`
   - `token`: (será preenchido após autenticação)

3. Configure o Bearer Token:
   - Vá em "Manage Environments"
   - Adicione um header com:
     - Key: `Authorization`
     - Value: `Bearer {{token}}`

## Endpoints

### 1. Autorizar Pagamento

- **Método**: POST
- **URL**: `/payments/authorize`
- **Permissão**: `SCOPE_payments:write`
- **Headers**:
  - `Authorization`: Bearer Token
  - `Content-Type`: application/json

**Exemplo de Request**:

```json
{
  "amount": 100.0,
  "currency": "BRL",
  "paymentMethod": "token_do_cartao",
  "customerId": "12345"
}
```

### 2. Capturar Pagamento

- **Método**: POST
- **URL**: `/payments/capture/{transactionId}`
- **Permissão**: `SCOPE_payments:write`
- **Headers**:
  - `Authorization`: Bearer Token
  - `Content-Type`: application/json

**Parâmetros**:

- `transactionId`: UUID da transação

### 3. Estornar Pagamento

- **Método**: POST
- **URL**: `/payments/refund/{transactionId}`
- **Permissão**: `SCOPE_payments:write`
- **Headers**:
  - `Authorization`: Bearer Token
  - `Content-Type`: application/json

**Parâmetros**:

- `transactionId`: UUID da transação
- `amount`: Valor a ser estornado (query parameter)

### 4. Consultar Pagamento

- **Método**: GET
- **URL**: `/payments/{id}`
- **Permissão**: `SCOPE_payments:read`
- **Headers**:
  - `Authorization`: Bearer Token

**Parâmetros**:

- `id`: UUID do pagamento

## Passo a Passo para Testar no Insomnia

1. **Configuração Inicial**:

   - Abra o Insomnia
   - Crie um novo projeto
   - Configure o ambiente conforme descrito acima

2. **Obter Token de Acesso**:

   - Acesse o Keycloak em `http://localhost:8080/auth`
   - Faça login e obtenha o token de acesso
   - Cole o token na variável `token` do ambiente

3. **Testar Endpoints**:

   - Crie uma nova requisição para cada endpoint
   - Configure os headers necessários
   - Preencha o corpo da requisição conforme os exemplos
   - Envie a requisição e verifique a resposta

4. **Dicas**:
   - Mantenha o token atualizado
   - Verifique as permissões necessárias para cada endpoint
   - Use o ambiente de sandbox para testes
   - Consulte a documentação do Swagger em `http://localhost:8080/api/swagger-ui.html` para mais detalhes

## Respostas de Erro

- **400**: Requisição inválida
- **401**: Não autorizado
- **404**: Recurso não encontrado
- **500**: Erro interno do servidor

## Observações

- A API está configurada para ambiente de desenvolvimento
- Use o ambiente sandbox para testes
- Mantenha suas credenciais seguras
- Consulte a documentação do Swagger para exemplos completos de requisições e respostas
