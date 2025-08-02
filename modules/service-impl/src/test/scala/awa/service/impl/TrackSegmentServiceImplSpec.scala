package awa.service.impl

import awa.generator.IdGenerator
import awa.model.TrackSegment
import awa.model.data.TrackSegmentId
import awa.persistence.TrackSegmentRepository
import awa.scalamock.IdGeneratorZIOStub
import awa.scalamock.ZIOStubBaseOperations
import awa.service.TrackSegmentService
import awa.testing.Spec
import awa.testing.generator.AwaGen
import awa.testing.generator.liveTrackSegmentInput
import awa.testing.generator.track
import org.scalamock.stubs.ZIOStubs
import zio.ZIO
import zio.ZLayer
import zio.test.*

object TrackSegmentServiceImplSpec extends Spec with IdGeneratorZIOStub with ZIOStubs with ZIOStubBaseOperations:

  def spec = suite(nameOf[TrackSegmentService])(
    test(s"It should add a ${nameOf[TrackSegment]} into the repository.") {
      val gen =
        for
          expectedId <- AwaGen.generateId
          input      <- AwaGen.liveTrackSegmentInput
          track      <- AwaGen.track
        yield (
          expectedId,
          input,
          TrackSegment(
            id = TrackSegmentId(expectedId),
            trackId = track.id,
            startedAt = input.startedAt,
            path = input.path,
            tagMap = input.tagMap,
            createdAt = None,
            order = None,
            timestamp = input.timestamp,
            horizontalAccuracy = input.horizontalAccuracy,
            verticalAccuracy = input.verticalAccuracy,
          ),
          track,
        )

      check(gen) { (newId, input, expected, track) =>
        for
          _      <- stubIdGenerator(newId)
          _      <- stubLayerWithZIO[TrackSegmentRepository].apply { repository =>
                      repository.add.returnsZIO(ZIO.succeed)
                    }
          result <- ZIO.serviceWithZIO[TrackSegmentService](_.add(input, track))
        yield assertTrue(
          result == expected,
        )
      }
    },
  ).provide(
    stubLayer[IdGenerator],
    stubLayer[TrackSegmentRepository],
    ZLayer.fromFunction(TrackSegmentServiceImpl.apply),
  )
