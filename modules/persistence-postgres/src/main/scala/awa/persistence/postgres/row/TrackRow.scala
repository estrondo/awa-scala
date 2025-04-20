package awa.persistence.postgres.row

import java.time.ZonedDateTime

case class TrackRow(
    id: String,
    accountId: String,
    startedAt: ZonedDateTime,
    deviceId: Option[String],
    deviceType: Option[String],
    createdAt: ZonedDateTime,
)
