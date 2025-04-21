package awa.testing.generator

import awa.input.PositionDataInput
import awa.model.data.PositionData
import zio.test.Gen

extension (gen: AwaGen)
  def randomPositionDataInput: Gen[Any, PositionDataInput] =
    for
      recordedAt         <- gen.nowZonedDateTime
      horizontalAccuracy <- Gen.int(0, 100)
      verticalAccuracy   <- Gen.int(0, 60)
    yield (recordedAt, horizontalAccuracy, verticalAccuracy)
