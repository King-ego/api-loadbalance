#!/bin/bash

BASE_URL="http://localhost:8099"
COOKIE_JAR="cookies.txt"

echo "Buscando usuários com sessão..."

response=$(curl -s -X GET "$BASE_URL/users" \
  -b $COOKIE_JAR)

echo "$response"