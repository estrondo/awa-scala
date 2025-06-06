package awa.grpc.v1.livetrack

import awa.crs.Geodesic
import awa.generator.TimeGenerator
import awa.input.TrackSegmentInput
import awa.model.Authorisation
import awa.model.Track
import awa.model.data.AccountId
import awa.scalamock.ZIOStubBaseOperations
import awa.service.TrackService
import awa.testing.Spec
import awa.testing.generator.AwaGen
import awa.testing.generator.randomAccountId
import awa.testing.generator.randomAuthorisation
import awa.testing.generator.randomTrack
import awa.testing.generator.randomTrackSegment
import awa.typeclass.ToShow
import awa.v1.generated.livetrack.CreateLiveTrackResponse
import awa.v1.generated.livetrack.LiveTrackCreated
import awa.v1.generated.livetrack.LiveTrackResponse
import awa.v1.generated.livetrack.LiveTrackSuccess
import awa.v1.generated.livetrack.ZioLivetrack.ZLiveTrackService
import awa.v1.generated.livetrack.randomCreateLiveTrackRequest
import awa.v1.generated.livetrack.randomLiveTrackRequest
import awa.v1.generated.livetrack.randomLiveTrackSegmentContent
import org.locationtech.jts.geom.GeometryFactory
import org.scalamock.stubs.ZIOStubs
import zio.ZIO
import zio.ZLayer
import zio.stream.ZStream
import zio.test.*

object LiveTrackServiceSpec extends Spec, ZIOStubs, ZIOStubBaseOperations:

  def spec = suite(typeOf(LiveTrackServiceImpl))(
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
          _      <- stubLayerWith[TrackService] { service =>
                      service.add.returns:
                        case (_, accountId) if accountId == authorisation.accountId => ZIO.succeed(track)

                    }
          result <- ZIO.serviceWithZIO[ZLiveTrackService[Authorisation]](x => x.create(request, authorisation))
        yield assertTrue(
          result == CreateLiveTrackResponse(
            traceId = request.traceId,
            content = CreateLiveTrackResponse.Content.LiveTrackCreated(
              LiveTrackCreated(
                id = ToShow(track.id),
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
          _      <- stubLayerWith[TrackService] { service =>
                      (service
                        .track(_: TrackSegmentInput, _: AccountId))
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
    stubLayer[TrackService],
    ZLayer.fromFunction((x: TimeGenerator, y: TrackService, z: GeometryFactory) => LiveTrackServiceImpl(x, y)(using z)),
  )
