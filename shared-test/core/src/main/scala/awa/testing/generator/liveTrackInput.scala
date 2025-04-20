package awa.testing.generator

import awa.input.LiveTrackInput
import zio.test.Gen

extension (gen: AwaGen)
  def randomLiveTrackInput: Gen[Any, LiveTrackInput] =
    for
      traceId    <- gen.keyGeneratorL32
      started    <- gen.nowZonedDateTime
      deviceId   <- Gen.option(Gen.stringN(10)(Gen.alphaNumericChar))
      deviceType <- Gen.option(Gen.stringN(16)(Gen.alphaNumericChar))
    yield LiveTrackInput(
      traceId = traceId,
      startedAt = started,
      deviceId = deviceId,
      deviceType = deviceType,
    )
