package awa.service.impl

import awa.generator.KeyGenerator
import awa.model.TrackSegment
import awa.persistence.TrackSegmentRepository
import awa.scalamock.KeyGeneratorZIOStub
import awa.scalamock.ZIOStubBaseOperations
import awa.service.LiveTrackSegmentService
import awa.testing.Spec
import awa.testing.input.LiveTrackSegmentInputFixture
import awa.testing.model.TrackFixture
import awa.testing.model.TrackSegmentFixture
import org.scalamock.stubs.ZIOStubs
import zio.ZIO
import zio.ZLayer
import zio.test.*

object LiveTrackSegmentServiceSpec extends Spec with KeyGeneratorZIOStub with ZIOStubs with ZIOStubBaseOperations:

  def spec = suite(nameOf[LiveTrackSegmentService])(
    test(s"It should add a ${nameOf[TrackSegment]} into the repository.") {
      val expectedTrackSegmentId = KeyGenerator.generateL16()
      val input                  = LiveTrackSegmentInputFixture.createRandom()
      val track                  = TrackFixture.createRandom()
      val expected               = TrackSegmentFixture
        .createFrom(input)
        .copy(
          track = track,
          id = expectedTrackSegmentId,
          order = None,
        )

      for
        _      <- stubKeyGeneratorL16(expectedTrackSegmentId)
        _      <- stubLayerWithZIO[TrackSegmentRepository].apply { repository =>
                    repository.add.returnsZIO(ZIO.succeed)
                  }
        result <- ZIO.serviceWithZIO[LiveTrackSegmentService](_.add(input, track))
      yield assertTrue(
        result == expected,
      )
    },
  ).provide(
    stubLayer[KeyGenerator],
    stubLayer[TrackSegmentRepository],
    ZLayer.fromFunction(LiveTrackSegmentService.apply),
  )
