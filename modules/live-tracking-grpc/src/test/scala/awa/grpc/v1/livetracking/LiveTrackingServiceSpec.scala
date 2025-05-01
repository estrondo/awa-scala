package awa.grpc.v1.livetracking

import awa.crs.Geodesic
import awa.generator.TimeGenerator
import awa.input.LiveTrackSegmentInput
import awa.model.Authorisation
import awa.model.Track
import awa.model.data.AccountId
import awa.scalamock.ZIOStubBaseOperations
import awa.service.LiveTrackService
import awa.testing.Spec
import awa.testing.generator.AwaGen
import awa.testing.generator.randomAccountId
import awa.testing.generator.randomAuthorisation
import awa.testing.generator.randomTrack
import awa.testing.generator.randomTrackSegment
import awa.v1.livetrack.CreateLiveTrackResponse
import awa.v1.livetrack.LiveTrackCreated
import awa.v1.livetrack.LiveTrackResponse
import awa.v1.livetrack.LiveTrackSuccess
import awa.v1.livetrack.ZioLivetrack.ZLiveTrackService
import awa.v1.livetrack.generator.randomCreateLiveTrackRequest
import awa.v1.livetrack.generator.randomLiveTrackRequest
import awa.v1.livetrack.generator.randomLiveTrackSegmentContent
import org.locationtech.jts.geom.GeometryFactory
import org.scalamock.stubs.ZIOStubs
import zio.ZIO
import zio.ZLayer
import zio.stream.ZStream
import zio.test.*

object LiveTrackingServiceSpec extends Spec, ZIOStubs, ZIOStubBaseOperations:

  def spec = suite(nameOf[ZLiveTrackService[Authorisation]])(
    test("It should create a new track") {
      val gen =
        for
          request       <- AwaGen.randomCreateLiveTrackRequest
          authorisation <- AwaGen.randomAuthorisation
          accountId     <- AwaGen.randomAccountId
          now           <- AwaGen.nowZonedDateTime
          track         <- AwaGen.randomTrack
        yield (
          request,
          authorisation,
          now,
          track.copy(
            accountId = authorisation.accountId,
          ),
        )

      check(gen) { (request, authorisation, now, track) =>
        for
          _      <- stubLayerWith[TimeGenerator] { generator =>
                      (() => generator.now()).returns(now)
                    }
          _      <- stubLayerWith[LiveTrackService] { service =>
                      service.add.returns:
                        case (_, accountId) if accountId == authorisation.accountId => ZIO.succeed(track)

                    }
          result <- ZIO.serviceWithZIO[ZLiveTrackService[Authorisation]](x => x.create(request, authorisation))
        yield assertTrue(
          result == CreateLiveTrackResponse(
            traceId = request.traceId,
            content = CreateLiveTrackResponse.Content.LiveTrackCreated(
              LiveTrackCreated(
                id = track.id.value,
                createdAt = track.createdAt.value.toEpochSecond,
              ),
            ),
          ),
        )
      }
    },
    test("It should track segments") {
      val gen =
        for
          request       <- AwaGen.randomLiveTrackRequest(AwaGen.randomLiveTrackSegmentContent)
          authorisation <- AwaGen.randomAuthorisation
          now           <- AwaGen.nowZonedDateTime
          trackSegment  <- AwaGen.randomTrackSegment
        yield (
          request,
          authorisation,
          now,
          trackSegment,
        )

      check(gen) { (request, authorisation, now, expectedTrackSegment) =>
        for
          _      <- stubLayerWith[TimeGenerator] { generator =>
                      (() => generator.now()).returns(now)
                    }
          _      <- stubLayerWith[LiveTrackService] { service =>
                      (service
                        .track(_: LiveTrackSegmentInput, _: AccountId))
                        .returns:
                          case (_, accountId) if accountId == authorisation.accountId => ZIO.succeed(expectedTrackSegment)
                          case (_, _)                                                 => ???
                    }
          result <-
            ZIO.serviceWithZIO[ZLiveTrackService[Authorisation]](x => x.track(ZStream(request), authorisation).runHead)
        yield assertTrue(
          result.is(_.some) == LiveTrackResponse(
            traceId = request.traceId,
            content = LiveTrackResponse.Content.Success(
              LiveTrackSuccess(
                numPositions = expectedTrackSegment.segment.value.getNumPoints(),
                length = Geodesic.length(expectedTrackSegment.segment),
              ),
            ),
          ),
        )
      }
    },
  ).provideSome(
    ZLayer.succeed(GeometryFactory()),
    stubLayer[TimeGenerator],
    stubLayer[LiveTrackService],
    ZLayer.fromFunction((x: TimeGenerator, y: LiveTrackService, z: GeometryFactory) =>
      LiveTrackingService(x, y)(using z),
    ),
  )
