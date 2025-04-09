package awa.service

import awa.MediaNotification
import awa.input.LiveTrackInput
import awa.input.TrackInput

trait MediaNotificationService[M[_]]:

  def search(track: LiveTrackInput): M[MediaNotification]

  def search(track: TrackInput): M[MediaNotification]
