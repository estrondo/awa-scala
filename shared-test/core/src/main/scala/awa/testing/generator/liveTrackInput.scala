package awa.testing.generator

import awa.input.LiveTrackInput
import awa.model.data.StartedAt
import zio.test.Gen

extension (gen: AwaGen)
  def randomLiveTrackInput: Gen[Any, LiveTrackInput] =
    for
      traceId    <- gen.randomTraceId
      started    <- gen.nowZonedDateTime
      deviceId   <- Gen.option(gen.randomDeviceId)
      deviceType <- Gen.option(gen.randomDeviceType)
    yield LiveTrackInput(
      traceId = traceId,
      startedAt = StartedAt(started),
      deviceId = deviceId,
      deviceType = deviceType,
    )
