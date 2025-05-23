package awa.grpc.v1.livetrack

import awa.AwaException
import awa.EF
import awa.crs.Geodesic
import awa.ducktape.TransformTo
import awa.ducktape.fallible.IdValidator
import awa.ducktape.fallible.NotEmptyValidator
import awa.ducktape.fallible.StartedAtValidator
import awa.ducktape.given
import awa.generator.TimeGenerator
import awa.grpc.GrpcIO
import awa.grpc.errors.EmptyTracking
import awa.grpc.errors.InvalidCreateTrackRequest
import awa.grpc.errors.InvalidLiveTrackRequest
import awa.grpc.errors.UnableToCreateTrack
import awa.grpc.errors.UnableToTrack
import awa.input.TrackInput
import awa.input.TrackPositionInput
import awa.input.TrackSegmentInput
import awa.logWarn
import awa.model.Authorisation
import awa.model.Track
import awa.model.TrackPosition
import awa.model.TrackSegment
import awa.model.data.DeviceId
import awa.model.data.DeviceType
import awa.model.data.Segment
import awa.model.data.StartedAt
import awa.model.data.TagMap
import awa.model.data.TraceId
import awa.model.data.TrackId
import awa.service.TrackService
import awa.v1.generated.livetrack.CreateLiveTrackRequest
import awa.v1.generated.livetrack.CreateLiveTrackResponse
import awa.v1.generated.livetrack.ErrorNote
import awa.v1.generated.livetrack.LiveTrackCreated
import awa.v1.generated.livetrack.LiveTrackError
import awa.v1.generated.livetrack.LiveTrackPosition
import awa.v1.generated.livetrack.LiveTrackRequest
import awa.v1.generated.livetrack.LiveTrackResponse
import awa.v1.generated.livetrack.LiveTrackSegment
import awa.v1.generated.livetrack.LiveTrackSuccess
import awa.v1.generated.livetrack.StatisticsRequest
import awa.v1.generated.livetrack.StatisticsResponse
import awa.v1.generated.livetrack.ZioLivetrack
import io.github.arainko.ducktape.Field
import io.github.arainko.ducktape.into
import io.grpc.StatusException
import org.locationtech.jts.geom.GeometryFactory
import org.locationtech.jts.geom.LineString
import scala.annotation.unused
import zio.IO
import zio.ZIO
import zio.stream.Stream

object LiveTrackServiceImpl:

  def apply(
      timeGenerator: TimeGenerator,
      service: TrackService,
  )(using GeometryFactory): ZioLivetrack.ZLiveTrackService[Authorisation] =
    new:
      override def create(
          request: CreateLiveTrackRequest,
          authorisation: Authorisation,
      ): IO[StatusException, CreateLiveTrackResponse] =
        for either <- convertToLiveTrackInput(request).flatMap { input =>
                        service
                          .add(input, authorisation.accountId)
                          .mapError(convertToLiveTrackError(UnableToCreateTrack))
                      }.either
        yield CreateLiveTrackResponse(
          traceId = request.traceId,
          content = either match
            case Right(value) => CreateLiveTrackResponse.Content.LiveTrackCreated(convertToLiveTrackCreated(value))
            case Left(value)  => CreateLiveTrackResponse.Content.Error(value),
        )

      override def track(
          stream: Stream[StatusException, LiveTrackRequest],
          authorisation: Authorisation,
      ): Stream[StatusException, LiveTrackResponse] =
        stream.mapZIO(handleLiveTrackItem(authorisation))

      private def handleLiveTrackItem(
          authorisation: Authorisation,
      )(request: LiveTrackRequest): GrpcIO[LiveTrackResponse] =
        for either <- convertToTrackInput(request).flatMap {
                        case input: TrackSegmentInput  =>
                          service
                            .track(input, authorisation.accountId)
                            .map(convertToLiveTrackSuccess)
                            .mapError(convertToLiveTrackError(UnableToTrack))
                        case input: TrackPositionInput =>
                          service
                            .track(input, authorisation.accountId)
                            .map(convertToLiveTrackSuccess)
                            .mapError(convertToLiveTrackError(UnableToTrack))
                      }.either
        yield LiveTrackResponse(
          traceId = request.traceId,
          content = either match {
            case Right(success) => LiveTrackResponse.Content.Success(success)
            case Left(error)    => LiveTrackResponse.Content.Error(error)
          },
        )

      override def statistics(
          request: StatisticsRequest,
          context: Authorisation,
      ): IO[StatusException, StatisticsResponse] = ???

      private def convertToTrackInput(
          request: LiveTrackRequest,
      ): EF[LiveTrackError, TrackSegmentInput | TrackPositionInput] =
        request.content match {
          case LiveTrackRequest.Content.Segment(value)  => convertToLiveTrackSegmentInput(value, request)
          case LiveTrackRequest.Content.Position(value) => convertToLiveTrackPositionInput(value, request)
          case LiveTrackRequest.Content.Empty           => reportEmptyLiveTrackRequest(request)
        }

      private def convertToLiveTrackSegmentInput(
          value: LiveTrackSegment,
          request: LiveTrackRequest,
      ): EF[LiveTrackError, TrackSegmentInput] =
        ZIO
          .fromEither {
            val now                              = timeGenerator.now()
            val (validSegment, validSegmentData) =
              LiveTrackProtocol.extractSegment("segment", "segmentData")(value.data, request)

            request
              .into[TrackSegmentInput]
              .fallible
              .transform(
                Field.fallibleConst(
                  _.traceId,
                  IdValidator[TraceId]("traceId")(request.traceId),
                ),
                Field.fallibleConst(
                  _.segment,
                  validSegment,
                ),
                Field.fallibleConst(
                  _.segmentData,
                  validSegmentData,
                ),
                Field.fallibleConst(
                  _.startedAt,
                  StartedAtValidator.notAfter("startedAt", now)(request.timestamp),
                ),
                Field.fallibleConst(
                  _.trackId,
                  IdValidator[TrackId]("trackId", "")(request.trackId),
                ),
                Field.const(
                  _.tagMap,
                  TransformTo[TagMap](request.tags),
                ),
              )
              .left
              .map(AwaException.WithNote("Invalid LiveTrackSegment.", _))
          }
          .logWarn("Unable to convert the LiveTrackSegmentInput.")
          .mapError(convertToLiveTrackError(InvalidLiveTrackRequest))

      private def convertToLiveTrackPositionInput(
          value: LiveTrackPosition,
          request: LiveTrackRequest,
      ): EF[LiveTrackError, TrackPositionInput] =
        ZIO
          .fromEither {

            val now                            = timeGenerator.now()
            val (valPosition, valPositionData) =
              LiveTrackProtocol.extractPosition("position", "positionData")(value.data, request)
            request
              .into[TrackPositionInput]
              .fallible
              .transform(
                Field.fallibleConst(
                  _.traceId,
                  IdValidator[TraceId]("traceId")(request.traceId),
                ),
                Field.fallibleConst(
                  _.positionData,
                  valPositionData,
                ),
                Field.fallibleConst(
                  _.point,
                  valPosition,
                ),
                Field.fallibleConst(
                  _.startedAt,
                  StartedAtValidator.notAfter("startedAt", now)(request.timestamp),
                ),
                Field.const(
                  _.tagMap,
                  TransformTo[TagMap](request.tags),
                ),
              )
              .left
              .map(AwaException.WithNote("", _))
          }
          .logWarn("Unable to convert to LiveTrackPositionInput.")
          .mapError(convertToLiveTrackError(InvalidLiveTrackRequest))

      private def convertToLiveTrackSuccess(track: TrackSegment): LiveTrackSuccess =
        LiveTrackSuccess(
          numPositions = track.segment.value.getNumPoints,
          length = Geodesic.length(track.segment),
        )

      private def convertToLiveTrackSuccess(@unused track: TrackPosition): LiveTrackSuccess =
        LiveTrackSuccess(numPositions = 1)

      private def reportEmptyLiveTrackRequest(@unused request: LiveTrackRequest): EF[LiveTrackError, Nothing] =
        ZIO.fail(LiveTrackError(code = EmptyTracking))

      private def convertToLiveTrackInput(request: CreateLiveTrackRequest): EF[LiveTrackError, TrackInput] =
        ZIO
          .fromEither {
            val now = timeGenerator.now()
            request
              .into[TrackInput]
              .fallible
              .transform(
                Field.fallibleConst(
                  _.traceId,
                  IdValidator[TraceId]("traceId")(request.traceId),
                ),
                Field.fallibleConst(
                  _.startedAt,
                  StartedAtValidator.notAfter("startedAt", now)(request.timestamp),
                ),
                Field.fallibleConst(
                  _.deviceId,
                  NotEmptyValidator[DeviceId]("deviceId", "Device Id must be informed.")(request.deviceId),
                ),
                Field.fallibleConst(
                  _.deviceType,
                  NotEmptyValidator[DeviceType]("deviceType", "Device Type must be informed.")(request.deviceType),
                ),
              )
              .left
              .map(AwaException.WithNote("Invalid CreateLiveTrackRequest.", _))
          }
          .logWarn("Unable to convert to LiveTrackInput.")
          .mapError(convertToLiveTrackError(InvalidCreateTrackRequest))

      private def convertToLiveTrackCreated(track: Track): LiveTrackCreated =
        track
          .into[LiveTrackCreated]
          .transform(
            Field.default(_.unknownFields),
          )

      private def convertToLiveTrackError(code: String)(cause: AwaException): LiveTrackError =
        cause match
          case withNote: AwaException.WithNote =>
            LiveTrackError(
              code = code,
              message = cause.getMessage,
              notes = for note <- withNote.notes yield ErrorNote(note.note, note.description),
            )
          case _                               =>
            LiveTrackError(
              code = code,
              message = cause.getMessage,
            )
