package awa.persistence

import awa.IO
import awa.model.TrackSegment

trait TrackSegmentRepository:
  def add(segment: TrackSegment): IO[TrackSegment]
