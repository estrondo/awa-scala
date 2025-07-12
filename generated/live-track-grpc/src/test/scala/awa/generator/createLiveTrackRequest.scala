package awa.v1.generated.livetrack

import awa.testing.generator.AwaGen
import awa.testing.generator.client
import awa.testing.generator.platform
import awa.testing.generator.traceId
import awa.typeclass.ToString
import zio.test.Gen

extension (self: AwaGen)
  def createLiveTrackRequest: Gen[Any, CreateLiveTrackRequest] =
    for
      traceId  <- self.traceId
      now      <- self.nowZonedDateTime
      platform <- self.platform
      client   <- self.client
    yield CreateLiveTrackRequest(
      traceId = ToString(traceId),
      timestamp = now.toEpochSecond(),
      platform = platform.value,
      client = client.value,
    )
