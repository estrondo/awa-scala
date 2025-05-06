package awa.service

import awa.F
import awa.input.TrackSegmentInput
import awa.model.Track
import awa.model.TrackSegment

trait TrackSegmentService:

  def add(input: TrackSegmentInput, track: Track): F[TrackSegment]
