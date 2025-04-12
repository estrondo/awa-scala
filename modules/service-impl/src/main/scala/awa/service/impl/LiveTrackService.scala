package awa.service.impl

import awa.*
import awa.generator.KeyGenerator
import awa.generator.TimeGenerator
import awa.input.LiveTrackInput
import awa.model.Account
import awa.model.Track
import awa.persistence.TrackRepository
import awa.service.LiveTrackService
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
        _         <- ZIO.logDebug("Track was added.")
      yield added
    }.annotated(
      "track.traceId" -> track.traceId,
      "account.id"    -> account.id,
    )

    private def convert(input: LiveTrackInput, account: Account): IO[Track] =
      ZIO
        .attempt {
          Track(
            id = keyGenerator.generateL16(),
            account = account,
            startedAt = input.startedAt,
            deviceId = input.deviceId,
            deviceType = input.deviceType,
            createdAt = timeGenerator.now(),
          )
        }
        .mapErrorToAwa(AwaException.Unexpected("An unexpected erro has occurred.", _))
