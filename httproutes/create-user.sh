#!/bin/bash

BASE_URL="http://localhost:8099"
COOKIE_JAR="cookies.txt"

echo "Inserir os dados necessários..."

echo "Digite o username do usuário:"
read -r USERNAME

echo "Digite o email do usuário:"
read -r EMAIL

echo "Digite a senha do usuário:"
read -r PASSWORD

curl -s -X POST "$BASE_URL/users" \
  -H "Content-Type: application/json" \
  -b $COOKIE_JAR \
  -d "{
    \"username\": \"$USERNAME\",
    \"email\": \"$EMAIL\",
    \"password\": \"$PASSWORD\",
    \"confirmPassword\": \"$PASSWORD\"
  }"

echo "Usuario com Sucesso"