package awa.persistence

import awa.F
import awa.model.Track

trait TrackRepository:

  def add(track: Track): F[Track]
