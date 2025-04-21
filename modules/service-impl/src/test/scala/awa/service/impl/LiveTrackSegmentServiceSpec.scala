package awa.service.impl

import awa.generator.KeyGenerator
import awa.input.PositionDataInput
import awa.model.TrackSegment
import awa.model.data.PositionData
import awa.model.data.Segment
import awa.model.data.SegmentData
import awa.model.data.StartedAt
import awa.model.data.TagMap
import awa.model.data.TrackSegmentId
import awa.persistence.TrackSegmentRepository
import awa.scalamock.KeyGeneratorZIOStub
import awa.scalamock.ZIOStubBaseOperations
import awa.service.LiveTrackSegmentService
import awa.testing.Spec
import awa.testing.ducktape.DucktapeMap
import awa.testing.generator.AwaGen
import awa.testing.generator.randomLiveTrackSegmentInput
import awa.testing.generator.randomTrack
import org.scalamock.stubs.ZIOStubs
import zio.ZIO
import zio.ZLayer
import zio.test.*

object LiveTrackSegmentServiceSpec extends Spec with KeyGeneratorZIOStub with ZIOStubs with ZIOStubBaseOperations:

  def spec = suite(nameOf[LiveTrackSegmentService])(
    test(s"It should add a ${nameOf[TrackSegment]} into the repository.") {
      val gen =
        for
          expectedId <- AwaGen.keyGeneratorL32
          input      <- AwaGen.randomLiveTrackSegmentInput
          track      <- AwaGen.randomTrack
        yield (
          expectedId,
          input,
          TrackSegment(
            id = TrackSegmentId(expectedId),
            track = track,
            startedAt = StartedAt(input.startedAt),
            segment = Segment(input.segment),
            segmentData = SegmentData(input.segmentData.map(DucktapeMap[PositionDataInput, PositionData])),
            tagMap = TagMap(input.tagMap),
            order = None,
          ),
        )

      check(gen) { (newId, input, expected) =>
        for
          _      <- stubKeyGeneratorL32(newId)
          _      <- stubLayerWithZIO[TrackSegmentRepository].apply { repository =>
                      repository.add.returnsZIO(ZIO.succeed)
                    }
          result <- ZIO.serviceWithZIO[LiveTrackSegmentService](_.add(input, expected.track))
        yield assertTrue(
          result == expected,
        )
      }
    },
  ).provide(
    stubLayer[KeyGenerator],
    stubLayer[TrackSegmentRepository],
    ZLayer.fromFunction(LiveTrackSegmentService.apply),
  )
