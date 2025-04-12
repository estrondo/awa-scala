package awa.service.impl

import awa.Spec
import awa.generator.KeyGenerator
import awa.generator.TimeGenerator
import awa.input.LiveTrackInputFixture
import awa.model.AccountFixture
import awa.model.Track
import awa.model.TrackFixture
import awa.persistence.TrackRepository
import awa.scalamock.KeyGeneratorZIOStub
import awa.scalamock.ScalaMockZIO
import awa.scalamock.ZIOStubsLayer
import awa.service.LiveTrackService
import awa.service.impl
import java.time.ZonedDateTime
import org.scalamock.stubs.ZIOStubs
import zio.ZIO
import zio.ZLayer
import zio.test.*

object LiveTrackServiceSpec extends Spec with ZIOStubs with KeyGeneratorZIOStub with ZIOStubsLayer:

  def spec = suite(nameOf[LiveTrackService])(
    test(s"It should add a ${nameOf[Track]} into the repository.") {
      val expectedTrackId = KeyGenerator.generateL16()
      val now             = ZonedDateTime.now()
      val input           = LiveTrackInputFixture.createRandom()
      val account         = AccountFixture.createRandom()
      val expected        = TrackFixture
        .createFrom(input)
        .copy(
          account = account,
          createdAt = now,
          id = expectedTrackId,
        )

      for
        _      <- stubKeyGeneratorL16(expectedTrackId)
        _      <- ScalaMockZIO.stubLayerWith[TimeGenerator].apply { generator =>
                    (() => generator.now()).returns(now)
                  }
        _      <- ScalaMockZIO.stubLayerWithZIO[TrackRepository].apply { repository =>
                    repository.add.returnsZIO(ZIO.succeed)
                  }
        result <- ZIO.serviceWithZIO[LiveTrackService](_.add(input, account))
      yield assertTrue(
        result == expected,
      )
    },
  ).provide(
    stubLayer[KeyGenerator],
    stubLayer[TimeGenerator],
    stubLayer[TrackRepository],
    ZLayer.fromFunction(impl.LiveTrackService.apply),
  )
