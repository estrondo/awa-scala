package awa.persistence

import awa.MediaNotification
import awa.input.LiveTrackInput
import awa.input.TrackInput

trait MediaNotificationRepository[M[_]]:

  def search(track: LiveTrackInput): M[MediaNotification]

  def search(track: TrackInput): M[MediaNotification]
