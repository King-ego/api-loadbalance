#!/bin/bash
set -e

if [ -z "$(ls -A ${PGDATA})" ]; then
    echo "Iniciando pg_basebackup do primary..."
    until pg_basebackup -h primary-db -U replicator -D "${PGDATA}" -Fp -Xs -P -R; do
        echo "primary-db não está pronto, tentando novamente em 2s..."
        sleep 2
    done
    echo "pg_basebackup concluído!"
fi


chown -R postgres:postgres "${PGDATA}"
chmod 700 "${PGDATA}"

exec gosu postgres postgres -D "${PGDATA}"
