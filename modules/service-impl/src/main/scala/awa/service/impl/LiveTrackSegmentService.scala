package awa.service.impl

import awa.*
import awa.generator.IdGenerator
import awa.input.LiveTrackSegmentInput
import awa.model.Track
import awa.model.TrackSegment
import awa.model.data.TrackSegmentId
import awa.persistence.TrackSegmentRepository
import awa.service.LiveTrackSegmentService
import awa.typeclass.ToShow
import io.github.arainko.ducktape.*
import zio.ZIO

object LiveTrackSegmentService:

  def apply(
      idGenerator: IdGenerator,
      repository: TrackSegmentRepository,
  ): LiveTrackSegmentService = new LiveTrackSegmentService:
    override def add(input: LiveTrackSegmentInput, track: Track): F[TrackSegment] = {
      for
        trackSegment <- convert(input, track)
                          .logError("Unable to convert the input.")
        added        <- repository
                          .add(trackSegment)
                          .logError("Unable to add a new segment.")
        _            <- ZIO.logInfo("TrackSegment was added.")
      yield added
    }.annotated(
      "segment.traceId" -> ToShow(input.traceId),
      "track.id"        -> ToShow(track.id),
      "account.id"      -> ToShow(track.accountId),
    )

    private def convert(input: LiveTrackSegmentInput, track: Track): F[TrackSegment] =
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
