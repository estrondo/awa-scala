package awa.service.impl

import awa.*
import awa.generator.KeyGenerator
import awa.generator.TimeGenerator
import awa.input.LiveTrackInput
import awa.model.Account
import awa.model.Track
import awa.model.data.CreatedAt
import awa.model.data.DeviceId
import awa.model.data.DeviceType
import awa.model.data.StartedAt
import awa.model.data.TrackId
import awa.persistence.TrackRepository
import awa.service.LiveTrackService
import io.github.arainko.ducktape.Field
import io.github.arainko.ducktape.into
import zio.ZIO

object LiveTrackService:

  def apply(
      keyGenerator: KeyGenerator,
      timeGenerator: TimeGenerator,
      repository: TrackRepository,
  ): LiveTrackService = new LiveTrackService:

    override def add(track: LiveTrackInput, account: Account): IO[Track] = {
      for
        converted <- convert(track, account)
                       .logError("Unable to convert to track.")
        added     <- repository
                       .add(converted)
                       .logError("Unable to add track.")
        _         <- ZIO
                       .logDebug("Track was added.")
                       .annotated(
                         "track.id" -> added.id.value,
                       )
      yield added
    }.annotated(
      "track.traceId" -> track.traceId,
      "account.id"    -> account.id.value,
    )

    private def convert(input: LiveTrackInput, account: Account): IO[Track] =
      ZIO
        .attempt {
          input
            .into[Track]
            .transform(
              Field.const(_.id, TrackId(keyGenerator)),
              Field.const(_.createdAt, CreatedAt(timeGenerator.now())),
              Field.const(_.account, account),
            )
        }
        .mapErrorToAwa(AwaException.Conversion("Unable to convert to Track!", _))
