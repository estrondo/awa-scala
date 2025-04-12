package awa.persistence

import awa.MediaNotification
import awa.input.LiveTrackInput
import awa.input.LiveTrackSegmentInput

trait MediaNotificationRepository[M[_]]:

  def search(track: LiveTrackSegmentInput): M[MediaNotification]

  def search(track: LiveTrackInput): M[MediaNotification]
