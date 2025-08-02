package awa.testing.generator

import awa.model.data.AccountId
import awa.model.data.Client
import awa.model.data.Device
import awa.model.data.Email
import awa.model.data.IdentityProvider
import awa.model.data.Order
import awa.model.data.SegmentHorizontalAccuracy
import awa.model.data.SegmentPath
import awa.model.data.SegmentTimestamp
import awa.model.data.SegmentVerticalAccuracy
import awa.model.data.StartedAt
import awa.model.data.TagMap
import awa.model.data.Token
import awa.model.data.TraceId
import awa.model.data.TrackId
import awa.model.data.TrackSegmentId
import awa.testing.generator.lineString
import java.time.ZonedDateTime
import zio.test.Gen

extension (self: AwaGen)

  def accountId: Gen[Any, AccountId] =
    for value <- self.generateId yield AccountId(value)

  def email: Gen[Any, Email] =
    for
      user    <- Gen.string
      domain  <- Gen.alphaNumericString
      country <- Gen.stringN(2)(Gen.alphaChar)
    yield Email(s"$user@$domain.$country")

  def device: Gen[Any, Device] =
    val a = for v <- Gen.elements("android:1.23", "other:2.22.111") yield s"xaomi:$v"
    val b = for v <- Gen.elements("1.1.0", "2.1.1") yield s"iphone:$v"
    val c = for v <- Gen.elements("archlinux", "macos:10", "windows:11") yield s"desktop:$v"
    Gen.oneOf(a, b, c).map(Device.apply)

  def client: Gen[Any, Client] =
    val a = for v <- Gen.elements("1.1.0", "2.2.8") yield s"awa:$v"
    val b = for v <- Gen.elements("0.0.1", "11") yield s"other:$v"
    Gen.oneOf(a, b).map(Client.apply)

  def identityProvider: Gen[Any, IdentityProvider] =
    for value <- Gen.alphaNumericString yield IdentityProvider(value)

  def startedAt(when: Gen[Any, ZonedDateTime]): Gen[Any, StartedAt] =
    for value <- when
    yield StartedAt(value)

  def traceId: Gen[Any, TraceId] =
    for value <- Gen.uuid yield TraceId(value)

  def trackId: Gen[Any, TrackId] =
    for value <- self.generateId yield TrackId(value)

  def segmentPath(min: Int, max: Int): Gen[Any, SegmentPath] =
    for value <- self.lineString(0.000001, 0.00001, min, max) yield SegmentPath(value)

  def tagMap(kGen: Gen[Any, String], vGen: Gen[Any, String]): Gen[Any, TagMap] =
    tagMap(0, 20)(kGen, vGen)

  def tagMap(min: Int, max: Int)(kGen: Gen[Any, String], vGen: Gen[Any, String]): Gen[Any, TagMap] =
    for entries <- Gen.chunkOfBounded(min, max)(kGen <*> vGen)
    yield TagMap(Map.from(entries))

  def startedAtNow: Gen[Any, StartedAt] =
    for now <- self.nowZonedDateTime yield StartedAt(now)

  def token: Gen[Any, Token] =
    for bytes <- Gen.chunkOfBounded(1, 256)(Gen.byte) yield Token(bytes.toArray)

  def trackSegmentId: Gen[Any, TrackSegmentId] =
    for value <- self.generateId yield TrackSegmentId(value)

  def order: Gen[Any, Order] =
    for value <- Gen.int(0, 100) yield Order(value)

  def segmentTimestamp(min: Int, max: Int): Gen[Any, SegmentTimestamp] =
    def nextValue(now: ZonedDateTime) =
      for skip <- Gen.int(10, 60) yield (now.plusSeconds(skip), now)

    for
      n       <- Gen.int(min, max)
      initial <- self.nowZonedDateTime
      list    <- Gen.unfoldGenN(n)(initial)(nextValue)
    yield SegmentTimestamp(list)

  def segmentHorizontalAccuracy(min: Int, max: Int): Gen[Any, SegmentHorizontalAccuracy] =
    for
      n    <- Gen.int(min, max)
      list <- Gen.listOfN(n)(Gen.short(0, 256))
    yield SegmentHorizontalAccuracy(list)

  def segmentVerticalAccuracy(min: Int, max: Int): Gen[Any, SegmentVerticalAccuracy] =
    for
      n    <- Gen.int(min, max)
      list <- Gen.listOfN(n)(Gen.short(0, 256))
    yield SegmentVerticalAccuracy(list)
