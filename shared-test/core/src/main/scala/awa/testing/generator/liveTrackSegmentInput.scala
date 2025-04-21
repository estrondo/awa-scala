package awa.testing.generator

import awa.input.LiveTrackSegmentInput
import zio.test.Gen

extension (gen: AwaGen)
  def randomLiveTrackSegmentInput: Gen[Any, LiveTrackSegmentInput] =
    for
      traceId      <- Gen.stringBounded(0, 32)(Gen.alphaNumericChar)
      tagMap       <- Gen.mapOf(Gen.string, Gen.string)
      startedAt    <- gen.nowZonedDateTime
      deviceId     <- Gen.option(Gen.string1(Gen.char))
      deviceType   <- Gen.option(Gen.string1(Gen.char))
      segment      <- gen.lineString(-50, -40, -30, -10, 0.0001, 0.001, 10, 1000)
      positionData <- Gen.vectorOf(gen.randomPositionDataInput)
    yield LiveTrackSegmentInput(
      traceId = traceId,
      tagMap = tagMap,
      startedAt = startedAt,
      deviceId = deviceId,
      deviceType = deviceType,
      segment = segment,
      segmentData = positionData,
    )
