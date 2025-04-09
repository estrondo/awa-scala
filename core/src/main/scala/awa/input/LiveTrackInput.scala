package awa.input

import java.time.ZonedDateTime
import org.locationtech.jts.geom.LineString

case class LiveTrackInput(
    owner: String,
    tags: Map[String, String],
    when: ZonedDateTime,
    segment: LineString,
)
