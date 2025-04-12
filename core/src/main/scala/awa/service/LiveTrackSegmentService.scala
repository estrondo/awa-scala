package awa.service

import awa.IO
import awa.input.LiveTrackSegmentInput
import awa.model.Track
import awa.model.TrackSegment

trait LiveTrackSegmentService:

  def add(input: LiveTrackSegmentInput, track: Track): IO[TrackSegment]
