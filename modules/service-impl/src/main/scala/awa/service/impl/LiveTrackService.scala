package awa.service.impl

import awa.*
import awa.generator.IdGenerator
import awa.generator.TimeGenerator
import awa.input.LiveTrackInput
import awa.input.LiveTrackPositionInput
import awa.input.LiveTrackSegmentInput
import awa.model.Track
import awa.model.TrackPosition
import awa.model.TrackSegment
import awa.model.data.AccountId
import awa.model.data.CreatedAt
import awa.model.data.TrackId
import awa.persistence.TrackRepository
import awa.service.LiveTrackService
import awa.typeclass.ToShow
import io.github.arainko.ducktape.Field
import io.github.arainko.ducktape.into
import zio.ZIO

object LiveTrackService:

  def apply(
      idGenerator: IdGenerator,
      timeGenerator: TimeGenerator,
      repository: TrackRepository,
  ): LiveTrackService = new LiveTrackService:

    override def add(input: LiveTrackInput, accountId: AccountId): F[Track] = {
      for
        converted <- convert(input, accountId)
                       .logError("Unable to convert the input.")
        added     <- repository
                       .add(converted)
                       .logDebug("Track was added.")
                       .logError("Unable to add track.")
      yield added
    }.annotated(
      "traceId"   -> ToShow(input.traceId),
      "accountId" -> ToShow(accountId),
    )

    override def track(input: LiveTrackPositionInput, accountId: AccountId): F[TrackPosition] = ???

    override def track(input: LiveTrackSegmentInput, accountId: AccountId): F[TrackSegment] = ???

    private def convert(input: LiveTrackInput, accountId: AccountId): F[Track] =
      ZIO
        .attempt {
          input
            .into[Track]
            .transform(
              Field.const(_.id, TrackId(idGenerator)),
              Field.const(_.createdAt, CreatedAt(timeGenerator.now())),
              Field.const(_.accountId, accountId),
            )
        }
        .mapErrorToAwa(AwaException.Conversion("Unable to convert to Track!", _))
