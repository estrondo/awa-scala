package awa.testing.generator

import awa.model.TrackSegment
import awa.model.data.CreatedAt
import zio.test.Gen

extension (self: AwaGen)
  def randomTrackSegment: Gen[Any, TrackSegment] =
    for
      id          <- self.randomTrackSegmentId
      trackId     <- self.randomTrackId
      startedAt   <- self.startedAtNow
      segment     <- self.randomSegment(2, 20)
      segmentData <- self.randomSegmentData
      tagMap      <- self.tagMap(Gen.string, Gen.string)
      order       <- Gen.option(self.randomOrder)
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
