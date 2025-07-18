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
      platform    <- self.platform
      client      <- self.client
      lineString  <- self.lineString(-50, -40, -30, -10, 0.0001, 0.001, 10, 1000)
      segmentData <- self.segmentData
    yield TrackSegmentInput(
      trackId = trackId,
      traceId = traceId,
      tagMap = tagMap,
      startedAt = startedAt,
      platform = platform,
      client = client,
      segment = Segment(lineString),
      segmentData = segmentData,
    )
