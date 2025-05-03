package awa.service

import awa.F
import awa.input.LiveTrackSegmentInput
import awa.model.Track
import awa.model.TrackSegment

trait LiveTrackSegmentService:

  def add(input: LiveTrackSegmentInput, track: Track): F[TrackSegment]
