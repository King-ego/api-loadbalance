#!/bin/bash

BASE_URL="http://localhost:8099"
COOKIE_JAR="cookies.txt"

echo "Insira os dados necessários..."

echo "Digite o ID do usuário a ser adicionado:"
read -r USER_ID

echo "Digite o ID da empresa:"
read -r COMPANY_ID

echo "Tentando adicionar membro na empresa com sessão..."

response=$(curl -s -X POST "$BASE_URL/companies/join" \
  -H "Content-Type: application/json" \
  -b $COOKIE_JAR \
  -d "{
    \"userId\": \"$USER_ID\",
    \"companyId\": \"$COMPANY_ID\"
  }")

echo "$response"