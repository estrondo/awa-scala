package awa.service.impl

import awa.AwaException
import awa.MediaNotification
import awa.input.LiveTrackInput
import awa.input.LiveTrackSegmentInput
import awa.persistence.MediaNotificationRepository
import awa.service.MediaNotificationService
import scalaz.MonadError
import scalaz.syntax.all.*

object MediaNotificationService:

  def apply[M[_]](repository: MediaNotificationRepository[M])(using
      M: MonadError[M, AwaException],
  ): MediaNotificationService[M] =
    new MediaNotificationService[M]:
      override def search(track: LiveTrackSegmentInput): M[MediaNotification] =
        for track <- prepareQuery(track)
        yield track

      override def search(track: LiveTrackInput): M[MediaNotification] = ???

      private def prepareQuery(track: LiveTrackSegmentInput): M[Nothing] = ???
