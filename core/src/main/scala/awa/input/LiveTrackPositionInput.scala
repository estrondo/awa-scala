package awa.input

import java.time.ZonedDateTime
import org.locationtech.jts.geom.Point

case class LiveTrackPositionInput(
    traceId: String,
    tagMap: Map[String, String],
    startedAt: ZonedDateTime,
    deviceId: Option[String],
    deviceType: Option[String],
    point: Point,
    positionData: PositionDataInput,
)
