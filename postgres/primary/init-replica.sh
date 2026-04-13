#!/bin/bash
# Aguarda o primário estar pronto
until pg_basebackup -h primary-db -U replicator -D "$PGDATA" -Fp -Xs -P -R; do
    echo "Aguardando o primary-db..."
    sleep 2
done

echo "Replicação configurada com sucesso!"
