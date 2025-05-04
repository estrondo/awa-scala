package awa.testing.generator

import awa.model.Track
import awa.model.data.CreatedAt
import awa.model.data.StartedAt
import java.time.temporal.ChronoUnit
import scala.annotation.unused
import zio.test.Gen

extension (@unused awaGen: AwaGen)
  def randomTrack: Gen[Any, Track] =
    for
      trackId       <- AwaGen.randomTrackId
      accountId     <- AwaGen.randomAccountId
      startedAt     <- AwaGen.nowZonedDateTime
      deviceId      <- AwaGen.randomDeviceId
      deviceType    <- AwaGen.randomDeviceType
      startedBefore <- Gen.int(60, 90)
    yield Track(
      id = trackId,
      accountId = accountId,
      startedAt = StartedAt(startedAt),
      deviceId = deviceId,
      deviceType = deviceType,
      createdAt = CreatedAt(startedAt.minus(startedBefore, ChronoUnit.SECONDS)),
    )
