package awa.service.impl

import awa.AwaException
import awa.F
import awa.annotated
import awa.generator.IdGenerator
import awa.input.TrackSegmentInput
import awa.mapErrorToAwa
import awa.model.Track
import awa.model.TrackSegment
import awa.model.data.TrackSegmentId
import awa.persistence.TrackSegmentRepository
import awa.service.TrackSegmentService
import awa.typeclass.ToString
import io.github.arainko.ducktape.*
import zio.ZIO

object TrackSegmentServiceImpl:

  def apply(
      idGenerator: IdGenerator,
      repository: TrackSegmentRepository,
  ): TrackSegmentService = new TrackSegmentService:
    override def add(input: TrackSegmentInput, track: Track): F[TrackSegment] = {
      for
        trackSegment <- convert(input, track)
                          .logError("Unable to convert the input.")
        added        <- repository
                          .add(trackSegment)
                          .logError("Unable to add a new segment.")
        _            <- ZIO.logInfo("TrackSegment was added.")
      yield added
    }.annotated(
      "segment.traceId" -> ToString(input.traceId),
      "track.id"        -> ToString(track.id),
      "account.id"      -> ToString(track.accountId),
    )

    private def convert(input: TrackSegmentInput, track: Track): F[TrackSegment] =
      ZIO
        .attempt {
          input
            .into[TrackSegment]
            .transform(
              Field.const(_.id, TrackSegmentId(idGenerator)),
              Field.const(_.order, None),
              Field.const(_.trackId, track.id),
              Field.const(_.createdAt, None),
            )
        }
        .mapErrorToAwa(AwaException.Conversion("Unable to convert to TrackSegment!", _))
