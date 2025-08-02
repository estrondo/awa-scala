package awa.testing.generator

import awa.model.Track
import awa.model.data.CreatedAt
import awa.model.data.StartedAt
import java.time.temporal.ChronoUnit
import zio.test.Gen

extension (self: AwaGen)
  def track: Gen[Any, Track] =
    for
      trackId       <- self.trackId
      accountId     <- self.accountId
      startedAt     <- self.nowZonedDateTime
      device        <- self.device
      client        <- self.client
      startedBefore <- Gen.int(60, 90)
    yield Track(
      id = trackId,
      accountId = accountId,
      startedAt = StartedAt(startedAt),
      device = device,
      client = client,
      createdAt = CreatedAt(startedAt.minus(startedBefore, ChronoUnit.SECONDS)),
    )
