package awa.persistence.postgres.row

import java.time.ZonedDateTime
import java.util.UUID

case class TrackRow(
    id: UUID,
    accountId: UUID,
    startedAt: ZonedDateTime,
    platform: String,
    client: String,
    createdAt: ZonedDateTime,
)
