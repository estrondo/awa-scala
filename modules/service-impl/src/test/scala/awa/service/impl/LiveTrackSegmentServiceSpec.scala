package awa.service.impl

import awa.generator.KeyGenerator
import awa.model.Track
import awa.model.TrackSegment
import awa.persistence.TrackSegmentRepository
import awa.scalamock.KeyGeneratorZIOStub
import awa.scalamock.ZIOStubBaseOperations
import awa.service.LiveTrackSegmentService
import awa.testing.Spec
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
            id = expectedId,
            track = track,
            startedAt = input.startedAt,
            segment = input.segment,
            positionData = input.positionData,
            tagMap = input.tagMap,
            order = None,
          ),
        )

      check(gen) { (newId, input, expected) =>
        for
          _      <- stubKeyGeneratorL16(newId)
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
