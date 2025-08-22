#!/bin/bash

# Script para configurar o Keycloak com as novas roles e permissões
# Execute este script após o Keycloak estar rodando

echo "🚀 Configurando Keycloak para WiPay Gateway..."

# Variáveis de configuração
KEYCLOAK_URL="http://localhost:8180"
REALM="gateway"
ADMIN_USERNAME="admin"
ADMIN_PASSWORD="admin"
CLIENT_ID="gateway-client"
CLIENT_SECRET="gateway-secret-key-2024"

# Aguardar o Keycloak estar disponível
echo "⏳ Aguardando Keycloak estar disponível..."
until curl -s "$KEYCLOAK_URL/health" > /dev/null 2>&1; do
    echo "   Aguardando..."
    sleep 5
done
echo "✅ Keycloak está disponível!"

# Obter token de admin
echo "🔑 Obtendo token de administrador..."
ADMIN_TOKEN=$(curl -s -X POST "$KEYCLOAK_URL/realms/master/protocol/openid-connect/token" \
    -H "Content-Type: application/x-www-form-urlencoded" \
    -d "grant_type=password&client_id=admin-cli&username=$ADMIN_USERNAME&password=$ADMIN_PASSWORD" \
    | jq -r '.access_token')

if [ "$ADMIN_TOKEN" = "null" ] || [ -z "$ADMIN_TOKEN" ]; then
    echo "❌ Erro ao obter token de admin. Verifique as credenciais."
    exit 1
fi
echo "✅ Token de admin obtido!"

# Criar realm se não existir
echo "🏗️ Criando realm '$REALM'..."
curl -s -X POST "$KEYCLOAK_URL/admin/realms" \
    -H "Authorization: Bearer $ADMIN_TOKEN" \
    -H "Content-Type: application/json" \
    -d "{
        \"realm\": \"$REALM\",
        \"enabled\": true,
        \"displayName\": \"WiPay Gateway\",
        \"displayNameHtml\": \"<div class=\\\"kc-logo-text\\\"><span>WiPay Gateway</span></div>\"
    }" > /dev/null

if [ $? -eq 0 ]; then
    echo "✅ Realm '$REALM' criado!"
else
    echo "ℹ️ Realm '$REALM' já existe ou erro na criação."
fi

# Criar cliente
echo "🔐 Criando cliente '$CLIENT_ID'..."
curl -s -X POST "$KEYCLOAK_URL/admin/realms/$REALM/clients" \
    -H "Authorization: Bearer $ADMIN_TOKEN" \
    -H "Content-Type: application/json" \
    -d "{
        \"clientId\": \"$CLIENT_ID\",
        \"enabled\": true,
        \"publicClient\": false,
        \"clientAuthenticatorType\": \"client-secret\",
        \"secret\": \"$CLIENT_SECRET\",
        \"redirectUris\": [
            \"http://localhost:8080/*\",
            \"http://localhost:8180/*\",
            \"http://localhost:3000/*\"
        ],
        \"webOrigins\": [
            \"http://localhost:8080\",
            \"http://localhost:3000\",
            \"+\"
        ],
        \"standardFlowEnabled\": true,
        \"directAccessGrantsEnabled\": true,
        \"serviceAccountsEnabled\": true,
        \"authorizationServicesEnabled\": true
    }" > /dev/null

if [ $? -eq 0 ]; then
    echo "✅ Cliente '$CLIENT_ID' criado!"
else
    echo "ℹ️ Cliente '$CLIENT_ID' já existe ou erro na criação."
fi

# Obter ID do cliente
echo "🔍 Obtendo ID do cliente..."
CLIENT_UUID=$(curl -s -X GET "$KEYCLOAK_URL/admin/realms/$REALM/clients?clientId=$CLIENT_ID" \
    -H "Authorization: Bearer $ADMIN_TOKEN" \
    | jq -r '.[0].id')

if [ "$CLIENT_UUID" = "null" ] || [ -z "$CLIENT_UUID" ]; then
    echo "❌ Erro ao obter ID do cliente."
    exit 1
fi
echo "✅ ID do cliente: $CLIENT_UUID"

# Criar roles do cliente
echo "👥 Criando roles do cliente..."

# Array de roles
declare -a ROLES=(
    "payments:read:Permissão para consultar pagamentos"
    "payments:write:Permissão para criar, atualizar e deletar pagamentos"
    "customers:read:Permissão para consultar clientes"
    "customers:write:Permissão para criar, atualizar e deletar clientes"
    "credit_cards:read:Permissão para consultar cartões de crédito"
    "credit_cards:write:Permissão para criar, atualizar e deletar cartões de crédito"
    "transactions:read:Permissão para consultar logs de transações"
)

for role in "${ROLES[@]}"; do
    IFS=':' read -r roleName roleDescription <<< "$role"
    
    echo "   Criando role: $roleName"
    curl -s -X POST "$KEYCLOAK_URL/admin/realms/$REALM/clients/$CLIENT_UUID/roles" \
        -H "Authorization: Bearer $ADMIN_TOKEN" \
        -H "Content-Type: application/json" \
        -d "{
            \"name\": \"$roleName\",
            \"description\": \"$roleDescription\"
        }" > /dev/null
    
    if [ $? -eq 0 ]; then
        echo "     ✅ Role '$roleName' criada!"
    else
        echo "     ℹ️ Role '$roleName' já existe ou erro na criação."
    fi
done

# Criar usuário admin
echo "👤 Criando usuário admin..."
curl -s -X POST "$KEYCLOAK_URL/admin/realms/$REALM/users" \
    -H "Authorization: Bearer $ADMIN_TOKEN" \
    -H "Content-Type: application/json" \
    -d "{
        \"username\": \"admin\",
        \"enabled\": true,
        \"emailVerified\": true,
        \"firstName\": \"Admin\",
        \"lastName\": \"User\",
        \"email\": \"admin@wipay.com\",
        \"credentials\": [
            {
                \"type\": \"password\",
                \"value\": \"admin\",
                \"temporary\": false
            }
        ]
    }" > /dev/null

if [ $? -eq 0 ]; then
    echo "✅ Usuário admin criado!"
else
    echo "ℹ️ Usuário admin já existe ou erro na criação."
fi

# Obter ID do usuário admin
echo "🔍 Obtendo ID do usuário admin..."
ADMIN_USER_UUID=$(curl -s -X GET "$KEYCLOAK_URL/admin/realms/$REALM/users?username=admin" \
    -H "Authorization: Bearer $ADMIN_TOKEN" \
    | jq -r '.[0].id')

if [ "$ADMIN_USER_UUID" = "null" ] || [ -z "$ADMIN_USER_UUID" ]; then
    echo "❌ Erro ao obter ID do usuário admin."
    exit 1
fi
echo "✅ ID do usuário admin: $ADMIN_USER_UUID"

# Atribuir roles ao usuário admin
echo "🔑 Atribuindo roles ao usuário admin..."

# Obter todas as roles do cliente
CLIENT_ROLES=$(curl -s -X GET "$KEYCLOAK_URL/admin/realms/$REALM/clients/$CLIENT_UUID/roles" \
    -H "Authorization: Bearer $ADMIN_TOKEN" \
    | jq -r '.[].name')

# Atribuir cada role ao usuário
for roleName in $CLIENT_ROLES; do
    echo "   Atribuindo role: $roleName"
    
    # Obter representação da role
    ROLE_REPRESENTATION=$(curl -s -X GET "$KEYCLOAK_URL/admin/realms/$REALM/clients/$CLIENT_UUID/roles/$roleName" \
        -H "Authorization: Bearer $ADMIN_TOKEN")
    
    # Atribuir role ao usuário
    curl -s -X POST "$KEYCLOAK_URL/admin/realms/$REALM/users/$ADMIN_USER_UUID/role-mappings/clients/$CLIENT_UUID" \
        -H "Authorization: Bearer $ADMIN_TOKEN" \
        -H "Content-Type: application/json" \
        -d "[$ROLE_REPRESENTATION]" > /dev/null
    
    if [ $? -eq 0 ]; then
        echo "     ✅ Role '$roleName' atribuída!"
    else
        echo "     ℹ️ Role '$roleName' já atribuída ou erro na atribuição."
    fi
done

# Criar usuário regular
echo "👤 Criando usuário regular..."
curl -s -X POST "$KEYCLOAK_URL/admin/realms/$REALM/users" \
    -H "Authorization: Bearer $ADMIN_TOKEN" \
    -H "Content-Type: application/json" \
    -d "{
        \"username\": \"user\",
        \"enabled\": true,
        \"emailVerified\": true,
        \"firstName\": \"Regular\",
        \"lastName\": \"User\",
        \"email\": \"user@wipay.com\",
        \"credentials\": [
            {
                \"type\": \"password\",
                \"value\": \"user123\",
                \"temporary\": false
            }
        ]
    }" > /dev/null

if [ $? -eq 0 ]; then
    echo "✅ Usuário regular criado!"
else
    echo "ℹ️ Usuário regular já existe ou erro na criação."
fi

# Obter ID do usuário regular
echo "🔍 Obtendo ID do usuário regular..."
USER_UUID=$(curl -s -X GET "$KEYCLOAK_URL/admin/realms/$REALM/users?username=user" \
    -H "Authorization: Bearer $ADMIN_TOKEN" \
    | jq -r '.[0].id')

if [ "$USER_UUID" = "null" ] || [ -z "$USER_UUID" ]; then
    echo "❌ Erro ao obter ID do usuário regular."
    exit 1
fi
echo "✅ ID do usuário regular: $USER_UUID"

# Atribuir roles de leitura ao usuário regular
echo "🔑 Atribuindo roles de leitura ao usuário regular..."
READ_ROLES=("payments:read" "customers:read" "credit_cards:read")

for roleName in "${READ_ROLES[@]}"; do
    echo "   Atribuindo role: $roleName"
    
    # Obter representação da role
    ROLE_REPRESENTATION=$(curl -s -X GET "$KEYCLOAK_URL/admin/realms/$REALM/clients/$CLIENT_UUID/roles/$roleName" \
        -H "Authorization: Bearer $ADMIN_TOKEN")
    
    # Atribuir role ao usuário
    curl -s -X POST "$KEYCLOAK_URL/admin/realms/$REALM/users/$USER_UUID/role-mappings/clients/$CLIENT_UUID" \
        -H "Authorization: Bearer $ADMIN_TOKEN" \
        -H "Content-Type: application/json" \
        -d "[$ROLE_REPRESENTATION]" > /dev/null
    
    if [ $? -eq 0 ]; then
        echo "     ✅ Role '$roleName' atribuída!"
    else
        echo "     ℹ️ Role '$roleName' já atribuída ou erro na atribuição."
    fi
done

echo ""
echo "🎉 Configuração do Keycloak concluída!"
echo ""
echo "📋 Resumo da configuração:"
echo "   Realm: $REALM"
echo "   Cliente: $CLIENT_ID"
echo "   Secret: $CLIENT_SECRET"
echo "   URL: $KEYCLOAK_URL"
echo ""
echo "👥 Usuários criados:"
echo "   admin/admin (todas as permissões)"
echo "   user/user123 (apenas leitura)"
echo ""
echo "🔑 Para obter token de acesso:"
echo "   curl -X POST $KEYCLOAK_URL/realms/$REALM/protocol/openid-connect/token \\"
echo "     -H \"Content-Type: application/x-www-form-urlencoded\" \\"
echo "     -d \"grant_type=password&client_id=$CLIENT_ID&client_secret=$CLIENT_SECRET&username=admin&password=admin\""
echo ""
echo "🌐 Acesse o console admin em: $KEYCLOAK_URL"
