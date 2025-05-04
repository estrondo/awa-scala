package awa.persistence.postgres.row

import java.time.ZonedDateTime
import java.util.UUID

case class TrackRow(
    id: UUID,
    accountId: UUID,
    startedAt: ZonedDateTime,
    deviceId: Option[String],
    deviceType: Option[String],
    createdAt: ZonedDateTime,
)
