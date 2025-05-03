FROM docker.io/postgis/postgis:17-3.5-alpine

ENV POSTGRES_PASSWORD=awa \
  POSTGRES_USER=awa POSTGRES_DB=awa-test

RUN --mount=type=bind,source=/flyway.tar.gz,destination=/flyway.tar.gz \
  --mount=type=bind,source=flyway.toml,destination=/opt/flyway/conf/flyway.toml \
  --mount=type=bind,source=pg_prep,destination=/usr/local/bin/pg_prep \
  --mount=type=bind,source=migration,destination=/migration <<EOF

echo "### Installing OpenJDK"
apk update
apk add openjdk21
echo "### OpenJDK has been installed"


echo "### Installing Flyway"
mkdir -p /opt/flyway
tar -xzf /flyway.tar.gz --strip-components=1 -C /opt/flyway
echo "### Flyway has been installed"


echo "### Preparing Flyway"
cd /opt/flyway

echo "### Tricking Flyway to use Alpine's Java"
rm -rf jre
ln -s /usr/lib/jvm/default-jvm jre
echo "### Flyway has been prepared"

echo "### Running Flyway"
cd /opt/flyway
pg_prep init-cluster
su postgres -c "pg_prep start"
su postgres -c "pg_prep wait"
./flyway migrate
su postgres -c "pg_prep stop"
echo "### Flyway has been run"

echo "### Cleaning up"
cd /
rm -rfv /opt/flyway
apk del --purge openjdk21
echo "### Cleaned up"

EOF
