package awa.model

import java.time.ZonedDateTime

case class Track(
    id: String,
    account: Account,
    startedAt: ZonedDateTime,
    deviceId: Option[String],
    deviceType: Option[String],
    createdAt: ZonedDateTime,
)
