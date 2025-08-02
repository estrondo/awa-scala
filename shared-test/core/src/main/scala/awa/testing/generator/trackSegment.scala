package awa.testing.generator

import awa.model.TrackSegment
import awa.model.data.CreatedAt
import zio.test.Gen

extension (self: AwaGen)
  def trackSegment: Gen[Any, TrackSegment] =
    for
      id                 <- self.trackSegmentId
      trackId            <- self.trackId
      startedAt          <- self.startedAtNow
      segment            <- self.segmentPath(2, 20)
      tagMap             <- self.tagMap(Gen.string, Gen.string)
      order              <- Gen.option(self.order)
      seconds            <- Gen.option(Gen.int(5, 60))
      n                   = segment.size
      timestamp          <- self.segmentTimestamp(n, n)
      horizontalAccuracy <- self.segmentHorizontalAccuracy(n, n)
      verticalAccuracy   <- self.segmentVerticalAccuracy(n, n)
    yield TrackSegment(
      id = id,
      startedAt = startedAt,
      createdAt = seconds.map(x => CreatedAt(startedAt.value.plusSeconds(x))),
      trackId = trackId,
      path = segment,
      timestamp = timestamp,
      horizontalAccuracy = horizontalAccuracy,
      verticalAccuracy = verticalAccuracy,
      tagMap = tagMap,
      order = order,
    )
