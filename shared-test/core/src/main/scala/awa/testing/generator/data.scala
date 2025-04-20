package awa.testing.generator

import awa.data.PositionData
import awa.data.TagMap
import awa.model.data.DeviceId
import awa.model.data.DeviceType
import awa.model.data.StartedAt
import awa.model.data.TrackId
import java.time.ZonedDateTime
import scala.annotation.unused
import zio.test.Gen

extension (@unused awaGen: AwaGen)

  def randomEmail: Gen[Any, String] =
    for
      user    <- Gen.alphaNumericString
      domain  <- Gen.alphaNumericString
      country <- Gen.stringN(2)(Gen.alphaNumericChar)
    yield s"$user@$domain.$country"

  def randomDeviceType: Gen[Any, DeviceType] =
    for value <- Gen.stringBounded(3, 10)(Gen.alphaNumericChar)
    yield DeviceType(value)

  def randomDeviceId: Gen[Any, DeviceId] =
    for value <- Gen.stringN(10)(Gen.alphaNumericChar)
    yield DeviceId(value)

  def startedAt(when: Gen[Any, ZonedDateTime]): Gen[Any, StartedAt] =
    for value <- when
    yield StartedAt(value)

  def randomTrackId: Gen[Any, TrackId] =
    for value <- AwaGen.keyGeneratorL32
    yield TrackId(value)

  def randomPositionData: Gen[Any, PositionData] =
    for
      altitude           <- Gen.int(0, 8000)
      recordedAt         <- AwaGen.nowZonedDateTime
      horizontalAccuracy <- Gen.int(1, 60)
      altitudeAccuracy   <- Gen.int(1, 60)
    yield PositionData(
      altitude = altitude,
      recordedAt = recordedAt,
      horizontalAccuracy = horizontalAccuracy,
      altitudeAccuracy = altitudeAccuracy,
    )

  def tagMap(kGen: Gen[Any, String], vGen: Gen[Any, String]): Gen[Any, TagMap] =
    tagMap(0, Int.MaxValue)(kGen, vGen)

  def tagMap(min: Int, max: Int)(kGen: Gen[Any, String], vGen: Gen[Any, String]): Gen[Any, TagMap] =
    for entries <- Gen.chunkOfBounded(min, max)(kGen <*> vGen)
    yield Map.from(entries)
