package awa.input

import java.time.ZonedDateTime

case class LiveTrackInput(
    traceId: String,
    startedAt: ZonedDateTime,
    deviceId: Option[String],
    deviceType: Option[String],
)
