#!/usr/bin/env bash

# Build facts.
PORT=$(cat /pg-port)


# Importing 
. /usr/local/bin/docker-entrypoint.sh

waitPort() {
  local port=$1
  local count=0
  while [ ! -r "/var/run/postgresql/.s.PGSQL.$port" ] && [ $count -lt 10 ]; do
    echo "🐘 Waiting Postgres."
    sleep 1
    count=$((count + 1))
  done
}

echo "🐘 Preparing the Postgres environment."
docker_setup_env
docker_create_db_directories
docker_verify_minimum_env

echo "🐘 Starting Postgres."
docker_init_database_dir
pg_setup_hba_conf "postgres"


echo "🐘 Creating database"
postgres &
waitPort "5432"
docker_setup_db
docker_temp_server_stop

echo "🐘 Applying facts."
export PGPORT=$PORT
postgres &

waitPort "$PORT"

if [ ! -r "/var/run/postgresql/.s.PGSQL.$PORT" ]; then
  echo "❌ Postgres did not start." >&2
  exit 1
fi

docker_setup_db
echo "🐘 Now you have 10 seconds to execute your wonderful SQLs"
sleep 10

echo "🐘 Stoping Postgres."
docker_temp_server_stop