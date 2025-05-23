package awa.v1.generated.livetrack

import awa.testing.generator.AwaGen
import awa.testing.generator.authorisation
import awa.testing.generator.deviceId
import awa.testing.generator.deviceType
import awa.testing.generator.tagMap
import awa.testing.generator.traceId
import awa.testing.generator.trackId
import awa.typeclass.ToString
import com.google.protobuf.ByteString
import java.io.DataOutputStream
import zio.Random
import zio.test.Gen

private val (genX, genY, genAltitude, genAccuracy) = {
  val x        = Gen.fromZIO(Random.nextFloatBetween(-180, 180))
  val y        = Gen.fromZIO(Random.nextFloatBetween(-90, 90))
  val altitude = Gen.int(0, 600000)
  val accuracy = Gen.int(0, 256)
  (x, y, altitude, accuracy)
}

extension (self: AwaGen)

  def grpcSegmentData: Gen[Any, ByteString] =
    for
      x                  <- genX
      y                  <- genY
      altitude           <- genAccuracy
      offset             <- Gen.int(0, 3600)
      horizontalAccuracy <- genAccuracy
      verticalAccuracy   <- genAccuracy
    yield
      val output     = ByteString.newOutput()
      val dataOutput = DataOutputStream(output)
      dataOutput.writeFloat(x)
      dataOutput.writeFloat(y)
      dataOutput.writeShort(altitude)
      dataOutput.writeShort(offset & 0x3ff)
      dataOutput.writeByte(horizontalAccuracy)
      dataOutput.writeByte(verticalAccuracy)
      output.toByteString()

  def grpcPositionData: Gen[Any, ByteString] =
    for
      x                  <- genX
      y                  <- genY
      altitude           <- genAltitude
      horizontalAccuracy <- genAccuracy
      verticalAccuracy   <- genAccuracy
    yield
      val output    = ByteString.newOutput()
      val dataOuput = DataOutputStream(output)
      dataOuput.writeFloat(x)
      dataOuput.writeFloat(y)
      dataOuput.writeShort(altitude)
      dataOuput.writeByte(horizontalAccuracy)
      dataOuput.writeByte(verticalAccuracy)
      output.toByteString()

  def liveTrackSegment: Gen[Any, LiveTrackSegment] =
    for data <- Gen.chunkOfBounded(2, 60)(self.grpcSegmentData)
    yield LiveTrackSegment(
      data = data,
    )

  def liveTrackRequestContent: Gen[Any, LiveTrackRequest.Content] =
    Gen.oneOf(liveTrackRequestPositionContent, liveTrackSegmentContent)

  def liveTrackRequestPositionContent: Gen[Any, LiveTrackRequest.Content] =
    liveTrackPosition.map(LiveTrackRequest.Content.Position.apply)

  def liveTrackSegmentContent: Gen[Any, LiveTrackRequest.Content] =
    liveTrackSegment.map(LiveTrackRequest.Content.Segment.apply)

  def liveTrackPosition: Gen[Any, LiveTrackPosition] =
    for data <- self.grpcPositionData yield LiveTrackPosition(data)

  def liveTrackRequest(gen: Gen[Any, LiveTrackRequest.Content]): Gen[Any, LiveTrackRequest] =
    for
      traceId          <- self.traceId
      trackId          <- self.trackId
      authorisation    <- self.authorisation
      tagMap           <- self.tagMap(Gen.string, Gen.string)
      deviceId         <- self.deviceId
      deviceType       <- self.deviceType
      liveTrackSegment <- self.liveTrackSegment
      firmwareVersion  <- Gen.string
      authorisation    <- self.authorisation
      content          <- gen
      now              <- self.nowZonedDateTime
    yield LiveTrackRequest(
      traceId = ToString(traceId),
      trackId = ToString(trackId),
      tags = tagMap.value,
      timestamp = now.toEpochSecond(),
      deviceId = deviceId.value,
      deviceType = deviceType.value,
      firmwareVersion = firmwareVersion,
      content = content,
    )
