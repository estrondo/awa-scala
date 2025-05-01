package awa.testing.generator

import awa.model.TrackSegment
import zio.test.Gen

extension (self: AwaGen)
  def randomTrackSegment: Gen[Any, TrackSegment] =
    for
      id          <- self.randomTrackSegmentId
      track       <- self.randomTrack
      startedAt   <- self.startedAtNow
      segment     <- self.randomSegment(2, 20)
      segmentData <- self.randomSegmentData
      tagMap      <- self.tagMap(Gen.string, Gen.string)
      order       <- Gen.option(self.randomOrder)
    yield TrackSegment(
      id = id,
      startedAt = startedAt,
      track = track,
      segment = segment,
      segmentData = segmentData,
      tagMap = tagMap,
      order = order,
    )
