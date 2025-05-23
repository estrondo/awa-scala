package awa.testing.generator

import awa.input.TrackInput
import awa.model.data.StartedAt
import zio.test.Gen

extension (self: AwaGen)
  def liveTrackInput: Gen[Any, TrackInput] =
    for
      traceId    <- self.traceId
      started    <- self.nowZonedDateTime
      deviceId   <- self.deviceId
      deviceType <- self.deviceType
    yield TrackInput(
      traceId = traceId,
      startedAt = StartedAt(started),
      deviceId = deviceId,
      deviceType = deviceType,
    )
