package awa.input

import awa.model.Account
import java.time.ZonedDateTime

case class LiveTrackInput(
    traceId: String,
    account: Account,
    startedAt: ZonedDateTime,
    deviceId: Option[String],
    deviceType: Option[String],
)
