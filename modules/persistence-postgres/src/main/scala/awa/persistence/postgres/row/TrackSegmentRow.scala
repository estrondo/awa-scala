package awa.persistence.postgres.row

import java.time.ZonedDateTime
import java.util.UUID
import org.locationtech.jts.geom.LineString

final case class TrackSegmentRow(
    id: UUID,
    trackId: UUID,
    segment: LineString,
    startedAt: ZonedDateTime,
    createdAt: ZonedDateTime,
    ord: Option[Int],
)
