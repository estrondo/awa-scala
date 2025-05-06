package awa.service

import awa.F
import awa.input.TrackInput
import awa.input.TrackPositionInput
import awa.input.TrackSegmentInput
import awa.model.Track
import awa.model.TrackPosition
import awa.model.TrackSegment
import awa.model.data.AccountId

trait TrackService:

  def add(input: TrackInput, accountId: AccountId): F[Track]

  def track(input: TrackSegmentInput, accountId: AccountId): F[TrackSegment]

  def track(input: TrackPositionInput, accountId: AccountId): F[TrackPosition]
