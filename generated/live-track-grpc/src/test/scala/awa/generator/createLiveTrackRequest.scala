package awa.v1.generated.livetrack

import awa.testing.generator.AwaGen
import awa.testing.generator.randomDeviceId
import awa.testing.generator.randomDeviceType
import awa.testing.generator.randomTraceId
import awa.typeclass.ToShow
import zio.test.Gen

extension (gen: AwaGen)
  def randomCreateLiveTrackRequest: Gen[Any, CreateLiveTrackRequest] =
    for
      traceId    <- gen.randomTraceId
      now        <- gen.nowZonedDateTime
      deviceId   <- gen.randomDeviceId
      deviceType <- gen.randomDeviceType
    yield CreateLiveTrackRequest(
      traceId = ToShow(traceId),
      timestamp = now.toEpochSecond(),
      deviceId = deviceId.value,
      deviceType = deviceType.value,
    )
