package awa.centre

import awa.F
import awa.input.LiveTrackInput
import awa.input.LiveTrackSegmentInput
import awa.model.Account
import awa.model.Track
import awa.model.TrackSegment

trait LiveTrackCentre:

  def add(track: LiveTrackInput, account: Account): F[Track]

  def add(segment: LiveTrackSegmentInput, account: Account): F[TrackSegment]
