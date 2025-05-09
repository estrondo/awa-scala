package awa.service.impl

import awa.generator.IdGenerator
import awa.generator.TimeGenerator
import awa.model.Track
import awa.model.data.CreatedAt
import awa.model.data.TrackId
import awa.persistence.TrackRepository
import awa.scalamock.IdGeneratorZIOStub
import awa.scalamock.ZIOStubBaseOperations
import awa.service.TrackService
import awa.service.impl
import awa.testing
import awa.testing.Spec
import awa.testing.generator.AwaGen
import awa.testing.generator.randomAccountId
import awa.testing.generator.randomLiveTrackInput
import java.time.ZonedDateTime
import org.scalamock.stubs.ZIOStubs
import zio.ZIO
import zio.ZLayer
import zio.test.*

object TrackServiceImplSpec extends Spec with ZIOStubs with IdGeneratorZIOStub with ZIOStubBaseOperations:

  def spec = suite(nameOf[TrackService])(
    test(s"It should add a ${nameOf[Track]} into the repository.") {
      val gen =
        for
          expectedId <- AwaGen.generateId
          now        <- AwaGen.nowZonedDateTime
          trackInput <- AwaGen.randomLiveTrackInput
          accountId  <- AwaGen.randomAccountId
        yield (
          now,
          expectedId,
          trackInput,
          Track(
            id = TrackId(expectedId),
            accountId = accountId,
            startedAt = trackInput.startedAt,
            deviceId = trackInput.deviceId,
            deviceType = trackInput.deviceType,
            createdAt = CreatedAt(now),
          ),
        )

      check(gen) { (now, expectedId, input, expectedTrack) =>
        for
          _      <- stubIdGenerator(expectedId)
          _      <- stubLayerWith[TimeGenerator].apply { generator =>
                      (() => generator.now()).returns(now)
                    }
          _      <- stubLayerWithZIO[TrackRepository].apply { repository =>
                      repository.add.returnsZIO(ZIO.succeed)
                    }
          result <- ZIO.serviceWithZIO[TrackService](_.add(input, expectedTrack.accountId))
        yield assertTrue(
          result == expectedTrack,
        )
      }
    } @@ TestAspect.samples(400),
  ).provide(
    stubLayer[IdGenerator],
    stubLayer[TimeGenerator],
    stubLayer[TrackRepository],
    ZLayer.fromFunction(TrackServiceImpl.apply),
  )
