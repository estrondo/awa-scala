package awa.testing.generator

import java.time.ZonedDateTime
import zio.test.Gen

extension (gen: AwaGen)
  def randomPositionDataInput: Gen[Any, (ZonedDateTime, Int, Int)] =
    for
      recordedAt         <- gen.nowZonedDateTime
      horizontalAccuracy <- Gen.int(0, 100)
      verticalAccuracy   <- Gen.int(0, 60)
    yield (recordedAt, horizontalAccuracy, verticalAccuracy)
