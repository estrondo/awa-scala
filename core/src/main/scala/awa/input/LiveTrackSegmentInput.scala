package awa.input

import awa.data.PositionData
import awa.data.TagMap
import java.time.ZonedDateTime
import org.locationtech.jts.geom.LineString

case class LiveTrackSegmentInput(
    traceId: String,
    tagMap: TagMap,
    startedAt: ZonedDateTime,
    deviceId: Option[String],
    deviceType: Option[String],
    segment: LineString,
    positionData: Seq[PositionData],
)
