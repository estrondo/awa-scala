package awa.input

import org.locationtech.jts.geom.LineString

case class TrackInput(
    owner: String,
    tags: Map[String, String],
    segments: Seq[LineString],
)
