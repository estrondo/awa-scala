package awa.service.impl

import awa.Spec
import awa.generator.KeyGenerator
import awa.input.LiveTrackSegmentInputFixture
import awa.model.TrackFixture
import awa.model.TrackSegment
import awa.model.TrackSegmentFixture
import awa.persistence.TrackSegmentRepository
import awa.scalamock.KeyGeneratorZIOStub
import awa.scalamock.ScalaMockZIO
import awa.scalamock.ZIOStubsLayer
import awa.service.LiveTrackSegmentService
import org.scalamock.stubs.ZIOStubs
import zio.ZIO
import zio.ZLayer
import zio.test.*

object LiveTrackSegmentServiceSpec extends Spec with KeyGeneratorZIOStub with ZIOStubs with ZIOStubsLayer:

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
        _      <- ScalaMockZIO.stubLayerWithZIO[TrackSegmentRepository].apply { repository =>
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
