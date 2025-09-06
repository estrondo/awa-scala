FROM {BASE}

ENV POSTGRES_USER={POSTGRES_USER} \
    POSTGRES_PASSWORD={POSTGRES_PASSWORD} \
    POSTGRES_DB={POSTGRES_DB}

RUN --mount=target=/pg-start.sh,source=pg-start.sh --mount=target=/pg-port,source=pg-port --network=host \
  su postgres -c 'bash pg-start.sh'