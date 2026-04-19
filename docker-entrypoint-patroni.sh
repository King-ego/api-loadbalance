#!/bin/bash
set -e

mkdir -p /data/patroni
chown -R postgres:postgres /data/patroni

exec gosu postgres patroni "$@"