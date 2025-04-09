package awa.input

import org.locationtech.jts.geom.Point

case class LivePositionInput(
    owner: String,
    tags: Map[String, String],
    position: Point,
)
