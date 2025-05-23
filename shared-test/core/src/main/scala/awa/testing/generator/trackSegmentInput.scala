package awa.testing.generator

import awa.input.TrackSegmentInput
import awa.model.data.Segment
import zio.test.Gen

extension (self: AwaGen)
  def liveTrackSegmentInput: Gen[Any, TrackSegmentInput] =
    for
      trackId     <- self.trackId
      traceId     <- self.traceId
      tagMap      <- self.tagMap(Gen.string, Gen.string)
      startedAt   <- self.startedAtNow
      deviceId    <- Gen.option(self.deviceId)
      deviceType  <- Gen.option(self.deviceType)
      lineString  <- self.lineString(-50, -40, -30, -10, 0.0001, 0.001, 10, 1000)
      segmentData <- self.segmentData
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
