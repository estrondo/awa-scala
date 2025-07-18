package awa.testing.generator

import awa.input.TrackInput
import awa.model.data.StartedAt
import zio.test.Gen

extension (self: AwaGen)
  def liveTrackInput: Gen[Any, TrackInput] =
    for
      traceId  <- self.traceId
      started  <- self.nowZonedDateTime
      platform <- self.platform
      client   <- self.client
    yield TrackInput(
      traceId = traceId,
      startedAt = StartedAt(started),
      platform = platform,
      client = client,
    )
