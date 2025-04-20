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
      account       <- AwaGen.randomAccount
      startedAt     <- AwaGen.nowZonedDateTime
      deviceId      <- Gen.option(AwaGen.randomDeviceId)
      deviceType    <- Gen.option(AwaGen.randomDeviceType)
      startedBefore <- Gen.int(60, 90)
    yield Track(
      id = trackId,
      account = account,
      startedAt = StartedAt(startedAt),
      deviceId = deviceId,
      deviceType = deviceType,
      createdAt = CreatedAt(startedAt.minus(startedBefore, ChronoUnit.SECONDS)),
    )
