#!/bin/bash
set -e

if [ -z "$(ls -A ${PGDATA})" ]; then
    echo "Start pg_basebackup of primary database..."
    until pg_basebackup -h primary-db -U replicator -D "${PGDATA}" -Fp -Xs -P -R; do
        echo "primary database not ready, try again in 2s..."
        sleep 2
    done
    echo "pg_basebackup completed!"
fi


chown -R postgres:postgres "${PGDATA}"
chmod 700 "${PGDATA}"

exec gosu postgres postgres -D "${PGDATA}"
