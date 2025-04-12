package awa.persistence

import awa.IO
import awa.model.Track

trait TrackRepository:

  def add(track: Track): IO[Track]
