package awa.service

import awa.F
import awa.input.LiveTrackInput
import awa.input.LiveTrackPositionInput
import awa.input.LiveTrackSegmentInput
import awa.model.Track
import awa.model.TrackPosition
import awa.model.TrackSegment
import awa.model.data.AccountId

trait LiveTrackService:

  def add(input: LiveTrackInput, accountId: AccountId): F[Track]

  def track(input: LiveTrackSegmentInput, accountId: AccountId): F[TrackSegment]

  def track(input: LiveTrackPositionInput, accountId: AccountId): F[TrackPosition]
