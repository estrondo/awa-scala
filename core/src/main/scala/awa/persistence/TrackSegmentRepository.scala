package awa.persistence

import awa.F
import awa.model.TrackSegment

trait TrackSegmentRepository:
  def add(segment: TrackSegment): F[TrackSegment]
