package awa.v1.livetrack.generator

import awa.testing.generator.AwaGen
import awa.testing.generator.randomAuthorisation
import awa.testing.generator.randomDeviceId
import awa.testing.generator.randomDeviceType
import awa.testing.generator.randomTraceId
import awa.testing.generator.randomTrackId
import awa.testing.generator.tagMap
import awa.typeclass.ToShow
import awa.v1.livetrack.LiveTrackPosition
import awa.v1.livetrack.LiveTrackRequest
import awa.v1.livetrack.LiveTrackSegment
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

  def randomGrpcSegmentData: Gen[Any, ByteString] =
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

  def randomGrpcPositionData: Gen[Any, ByteString] =
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

  def randomLiveTrackSegment: Gen[Any, LiveTrackSegment] =
    for data <- Gen.chunkOfBounded(2, 60)(self.randomGrpcSegmentData)
    yield LiveTrackSegment(
      data = data,
    )

  def randomLiveTrackRequestContent: Gen[Any, LiveTrackRequest.Content] =
    Gen.oneOf(randomLiveTrackRequestPositionContent, randomLiveTrackSegmentContent)

  def randomLiveTrackRequestPositionContent: Gen[Any, LiveTrackRequest.Content] =
    randomLiveTrackPosition.map(LiveTrackRequest.Content.Position.apply)

  def randomLiveTrackSegmentContent: Gen[Any, LiveTrackRequest.Content] =
    randomLiveTrackSegment.map(LiveTrackRequest.Content.Segment.apply)

  def randomLiveTrackPosition: Gen[Any, LiveTrackPosition] =
    for data <- self.randomGrpcPositionData yield LiveTrackPosition(data)

  def randomLiveTrackRequest(gen: Gen[Any, LiveTrackRequest.Content]): Gen[Any, LiveTrackRequest] =
    for
      traceId          <- self.randomTraceId
      trackId          <- self.randomTrackId
      authorisation    <- self.randomAuthorisation
      tagMap           <- self.tagMap(Gen.string, Gen.string)
      deviceId         <- self.randomDeviceId
      deviceType       <- self.randomDeviceType
      liveTrackSegment <- self.randomLiveTrackSegment
      firmwareVersion  <- Gen.string
      authorisation    <- self.randomAuthorisation
      content          <- gen
      now              <- self.nowZonedDateTime
    yield LiveTrackRequest(
      traceId = ToShow(traceId),
      trackId = ToShow(trackId),
      tags = tagMap.value,
      timestamp = now.toEpochSecond(),
      deviceId = deviceId.value,
      deviceType = deviceType.value,
      firmwareVersion = firmwareVersion,
      content = content,
    )
