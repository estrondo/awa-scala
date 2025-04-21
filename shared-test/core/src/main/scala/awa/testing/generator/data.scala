package awa.testing.generator

import awa.model.data.AccountId
import awa.model.data.DeviceId
import awa.model.data.DeviceType
import awa.model.data.Email
import awa.model.data.HorizontalAccuracy
import awa.model.data.IdentityProvider
import awa.model.data.PositionData
import awa.model.data.RecordedAt
import awa.model.data.StartedAt
import awa.model.data.TagMap
import awa.model.data.TrackId
import awa.model.data.VerticalAccuracy
import java.time.ZonedDateTime
import zio.test.Gen

extension (gen: AwaGen)

  def randomAccountId: Gen[Any, AccountId] =
    for value <- gen.keyGeneratorL16 yield AccountId(value)

  def randomEmail: Gen[Any, Email] =
    for
      user    <- Gen.string
      domain  <- Gen.alphaNumericString
      country <- Gen.stringN(2)(Gen.alphaChar)
    yield Email(s"$user@$domain.$country")

  def randomDeviceType: Gen[Any, DeviceType] =
    for value <- Gen.stringBounded(1, 16)(Gen.alphaNumericChar)
    yield DeviceType(value)

  def randomDeviceId: Gen[Any, DeviceId] =
    for value <- Gen.stringBounded(1, 16)(Gen.alphaNumericChar)
    yield DeviceId(value)

  def randomIdentityProvider: Gen[Any, IdentityProvider] =
    for value <- Gen.alphaNumericString yield IdentityProvider(value)

  def startedAt(when: Gen[Any, ZonedDateTime]): Gen[Any, StartedAt] =
    for value <- when
    yield StartedAt(value)

  def randomTrackId: Gen[Any, TrackId] =
    for value <- gen.keyGeneratorL32 yield TrackId(value)

  def randomPositionData: Gen[Any, PositionData] =
    for
      recordedAt         <- AwaGen.nowZonedDateTime
      horizontalAccuracy <- Gen.int(1, 60)
      verticalAccuracy   <- Gen.int(1, 60)
    yield PositionData(
      recordedAt = RecordedAt(recordedAt),
      horizontalAccuracy = HorizontalAccuracy(horizontalAccuracy),
      verticalAccuracy = VerticalAccuracy(verticalAccuracy),
    )

  def tagMap(kGen: Gen[Any, String], vGen: Gen[Any, String]): Gen[Any, TagMap] =
    tagMap(0, Int.MaxValue)(kGen, vGen)

  def tagMap(min: Int, max: Int)(kGen: Gen[Any, String], vGen: Gen[Any, String]): Gen[Any, TagMap] =
    for entries <- Gen.chunkOfBounded(min, max)(kGen <*> vGen)
    yield TagMap(Map.from(entries))
