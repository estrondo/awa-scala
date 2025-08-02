package awa.testing.generator

import awa.input.TrackSegmentInput
import awa.model.data.SegmentPath
import zio.test.Gen

extension (self: AwaGen)
  def liveTrackSegmentInput: Gen[Any, TrackSegmentInput] =
    for
      trackId            <- self.trackId
      traceId            <- self.traceId
      tagMap             <- self.tagMap(Gen.string, Gen.string)
      startedAt          <- self.startedAtNow
      device             <- self.device
      client             <- self.client
      lineString         <- self.lineString(-50, -40, -30, -10, 0.0001, 0.001, 10, 1000)
      n                   = lineString.getNumPoints()
      timestamp          <- self.segmentTimestamp(n, n)
      horizontalAccuracy <- self.segmentHorizontalAccuracy(n, n)
      verticalAccuracy   <- self.segmentVerticalAccuracy(n, n)
    yield TrackSegmentInput(
      trackId = trackId,
      traceId = traceId,
      tagMap = tagMap,
      startedAt = startedAt,
      device = device,
      client = client,
      path = SegmentPath(lineString),
      timestamp = timestamp,
      horizontalAccuracy = horizontalAccuracy,
      verticalAccuracy = verticalAccuracy,
    )
