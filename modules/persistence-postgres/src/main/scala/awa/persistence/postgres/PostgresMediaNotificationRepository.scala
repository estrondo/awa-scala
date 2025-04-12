package awa.persistence.postgres

import awa.MediaNotification
import awa.input.LiveTrackInput
import awa.input.TrackInput
import awa.persistence.MediaNotificationRepository
import io.getquill.*
import org.locationtech.jts.geom.Geometry

object PostgresMediaNotificationRepository:

  def apply(context: PostgresZioJdbcContext[SnakeCase]): MediaNotificationRepository[JdbcStream] =
    new MediaNotificationRepository[JdbcStream] with PostgresCodecs(context):
      override def search(track: LiveTrackInput): JdbcStream[MediaNotification] =
        ctx.stream {
          quote {
            query[MediaNotification]
              .filter(x => stIntersects(x.region, lift(track.segment: Geometry)))
          }
        }

      override def search(track: TrackInput): JdbcStream[MediaNotification] = ???
