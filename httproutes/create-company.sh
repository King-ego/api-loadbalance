#!/bin/bash

BASE_URL="http://localhost:8099"
COOKIE_JAR="cookies.txt"

echo "Insira os dados necessários..."

echo "Digite o nome da empresa:"
read -r NAME

echo "Digite a descrição da empresa:"
read -r DESCRIPTION

echo "Criando empresa com sessão..."

response=$(curl -s -X POST "$BASE_URL/companies" \
  -H "Content-Type: application/json" \
  -b $COOKIE_JAR \
  -d "{
    \"name\": \"$NAME\",
    \"description\": \"$DESCRIPTION\"
  }")

echo "$response"