package awa.testing.generator

import awa.model.TrackSegment
import awa.model.data.CreatedAt
import zio.test.Gen

extension (self: AwaGen)
  def trackSegment: Gen[Any, TrackSegment] =
    for
      id          <- self.trackSegmentId
      trackId     <- self.trackId
      startedAt   <- self.startedAtNow
      segment     <- self.segment(2, 20)
      segmentData <- self.segmentData
      tagMap      <- self.tagMap(Gen.string, Gen.string)
      order       <- Gen.option(self.order)
      seconds     <- Gen.option(Gen.int(5, 60))
    yield TrackSegment(
      id = id,
      startedAt = startedAt,
      createdAt = seconds.map(x => CreatedAt(startedAt.value.plusSeconds(x))),
      trackId = trackId,
      segment = segment,
      segmentData = segmentData,
      tagMap = tagMap,
      order = order,
    )
