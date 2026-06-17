#!/bin/bash

BASE_URL="http://localhost:8099"
COOKIE_JAR="cookies.txt"

echo "Insira os dados necessários..."

echo "Digite o ID do usuário:"
read -r USER_ID

echo "Digite o ID da empresa:"
read -r COMPANY_ID

echo "Tentando remover membro da empresa com sessão..."

response=$(curl -s -X POST "$BASE_URL/companies/leave" \
  -H "Content-Type: application/json" \
  -b $COOKIE_JAR \
  -d "{
    \"userId\": \"$USER_ID\",
    \"companyId\": \"$COMPANY_ID\"
  }")

echo "$response"