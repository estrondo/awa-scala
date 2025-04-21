package awa.input

import java.time.ZonedDateTime
import org.locationtech.jts.geom.LineString

case class LiveTrackSegmentInput(
    traceId: String,
    tagMap: Map[String, String],
    startedAt: ZonedDateTime,
    deviceId: Option[String],
    deviceType: Option[String],
    segment: LineString,
    segmentData: Seq[PositionDataInput],
)
