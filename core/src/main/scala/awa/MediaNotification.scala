package awa

import org.locationtech.jts.geom.Polygon

case class MediaNotification(
    id: String,
    owner: String,
    region: Polygon,
    groups: Option[Map[String, String]],
    tags: Option[Map[String, String]],
    mediaId: String,
)
