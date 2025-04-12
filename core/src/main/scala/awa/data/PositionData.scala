package awa.data

import java.time.ZonedDateTime

case class PositionData(
    altitude: Int,
    recordedAt: ZonedDateTime,
    horizontalAccuracy: Int,
    altitudeAccuracy: Int,
)
