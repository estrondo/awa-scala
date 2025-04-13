package awa.service.impl

import awa.generator.KeyGenerator
import awa.generator.TimeGenerator
import awa.model.Track
import awa.persistence.TrackRepository
import awa.scalamock.KeyGeneratorZIOStub
import awa.scalamock.ZIOStubBaseOperations
import awa.service.LiveTrackService
import awa.service.impl
import awa.testing
import awa.testing.Spec
import awa.testing.input.LiveTrackInputFixture
import awa.testing.model.AccountFixture
import awa.testing.model.TrackFixture
import java.time.ZonedDateTime
import org.scalamock.stubs.ZIOStubs
import zio.ZIO
import zio.ZLayer
import zio.test.*

object LiveTrackServiceSpec extends Spec with ZIOStubs with KeyGeneratorZIOStub with ZIOStubBaseOperations:

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
        _      <- stubLayerWith[TimeGenerator].apply { generator =>
                    (() => generator.now()).returns(now)
                  }
        _      <- stubLayerWithZIO[TrackRepository].apply { repository =>
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
