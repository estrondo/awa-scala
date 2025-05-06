package awa.testing.generator

import awa.input.TrackInput
import awa.model.data.StartedAt
import zio.test.Gen

extension (gen: AwaGen)
  def randomLiveTrackInput: Gen[Any, TrackInput] =
    for
      traceId    <- gen.randomTraceId
      started    <- gen.nowZonedDateTime
      deviceId   <- gen.randomDeviceId
      deviceType <- gen.randomDeviceType
    yield TrackInput(
      traceId = traceId,
      startedAt = StartedAt(started),
      deviceId = deviceId,
      deviceType = deviceType,
    )
