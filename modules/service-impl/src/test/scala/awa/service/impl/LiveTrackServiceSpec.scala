package awa.service.impl

import awa.generator.KeyGenerator
import awa.generator.TimeGenerator
import awa.model.Track
import awa.model.data.CreatedAt
import awa.model.data.DeviceId
import awa.model.data.DeviceType
import awa.model.data.StartedAt
import awa.model.data.TrackId
import awa.persistence.TrackRepository
import awa.scalamock.KeyGeneratorZIOStub
import awa.scalamock.ZIOStubBaseOperations
import awa.service.LiveTrackService
import awa.service.impl
import awa.testing
import awa.testing.Spec
import awa.testing.generator.AwaGen
import awa.testing.generator.randomAccount
import awa.testing.generator.randomLiveTrackInput
import java.time.ZonedDateTime
import org.scalamock.stubs.ZIOStubs
import zio.ZIO
import zio.ZLayer
import zio.test.*

object LiveTrackServiceSpec extends Spec with ZIOStubs with KeyGeneratorZIOStub with ZIOStubBaseOperations:

  def spec = suite(nameOf[LiveTrackService])(
    test(s"It should add a ${nameOf[Track]} into the repository.") {
      val gen =
        for
          expectedId <- AwaGen.keyGeneratorL16
          now        <- AwaGen.nowZonedDateTime
          trackInput <- AwaGen.randomLiveTrackInput
          account    <- AwaGen.randomAccount
        yield (
          now,
          expectedId,
          trackInput,
          Track(
            id = TrackId(expectedId),
            account = account,
            startedAt = StartedAt(trackInput.startedAt),
            deviceId = DeviceId(trackInput.deviceId),
            deviceType = DeviceType(trackInput.deviceType),
            createdAt = CreatedAt(now),
          ),
        )

      check(gen) { (now, expectedId, input, expectedTrack) =>
        for
          _      <- stubKeyGeneratorL16(expectedId)
          _      <- stubLayerWith[TimeGenerator].apply { generator =>
                      (() => generator.now()).returns(now)
                    }
          _      <- stubLayerWithZIO[TrackRepository].apply { repository =>
                      repository.add.returnsZIO(ZIO.succeed)
                    }
          result <- ZIO.serviceWithZIO[LiveTrackService](_.add(input, expectedTrack.account))
        yield assertTrue(
          result == expectedTrack,
        )
      }
    } @@ TestAspect.samples(400),
  ).provide(
    stubLayer[KeyGenerator],
    stubLayer[TimeGenerator],
    stubLayer[TrackRepository],
    ZLayer.fromFunction(impl.LiveTrackService.apply),
  )
