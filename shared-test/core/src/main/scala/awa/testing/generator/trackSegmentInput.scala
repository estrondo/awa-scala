package awa.testing.generator

import awa.input.TrackSegmentInput
import awa.model.data.Segment
import zio.test.Gen

extension (gen: AwaGen)
  def randomLiveTrackSegmentInput: Gen[Any, TrackSegmentInput] =
    for
      trackId     <- gen.randomTrackId
      traceId     <- gen.randomTraceId
      tagMap      <- gen.tagMap(Gen.string, Gen.string)
      startedAt   <- gen.startedAtNow
      deviceId    <- Gen.option(gen.randomDeviceId)
      deviceType  <- Gen.option(gen.randomDeviceType)
      lineString  <- gen.lineString(-50, -40, -30, -10, 0.0001, 0.001, 10, 1000)
      segmentData <- gen.randomSegmentData
    yield TrackSegmentInput(
      trackId = trackId,
      traceId = traceId,
      tagMap = tagMap,
      startedAt = startedAt,
      deviceId = deviceId,
      deviceType = deviceType,
      segment = Segment(lineString),
      segmentData = segmentData,
    )
