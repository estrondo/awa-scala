package awa.persistence.postgres.row

import java.time.ZonedDateTime
import org.locationtech.jts.geom.LineString

final case class TrackSegmentRow(
    id: String,
    trackId: String,
    segment: LineString,
    startedAt: ZonedDateTime,
    createdAt: ZonedDateTime,
    ord: Option[Int],
)
