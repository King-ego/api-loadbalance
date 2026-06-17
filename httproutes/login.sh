#!/bin/bash

BASE_URL="http://localhost:8099"
COOKIE_JAR="cookies.txt"

echo "Fazendo login..."

curl -s -X POST "$BASE_URL/auth/login" \
  -H "Content-Type: application/json" \
  -c $COOKIE_JAR \
  -d '{
    "email": "john3doe62@gmail.com",
    "password": "password"
  }'

echo "Cookie salvo em $COOKIE_JAR"