package awa.model

import awa.data.LineStringData
import awa.data.TagMap
import java.time.ZonedDateTime
import org.locationtech.jts.geom.LineString

case class TrackSegment(
    id: String,
    track: Track,
    startedAt: ZonedDateTime,
    segment: LineString,
    positionData: LineStringData,
    tagMap: TagMap,
    order: Option[Int],
)
