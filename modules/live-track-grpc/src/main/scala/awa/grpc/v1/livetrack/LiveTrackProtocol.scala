package awa.grpc.v1.livetrack

import awa.ducktape.TransformTo
import awa.grpc.protocol.GrpcZonedDateTimeProtocol
import awa.model.data.HorizontalAccuracy
import awa.model.data.Position
import awa.model.data.PositionData
import awa.model.data.RecordedAt
import awa.model.data.Segment
import awa.model.data.SegmentData
import awa.model.data.VerticalAccuracy
import awa.v1.generated.livetrack.LiveTrackRequest
import awa.validation.FailureNote
import awa.validation.Valid
import com.google.protobuf.ByteString
import java.io.DataInputStream
import org.locationtech.jts.geom.Coordinate
import org.locationtech.jts.geom.GeometryFactory

object LiveTrackProtocol:

  private val ZeroAltitude = 20000

  val ExpectedSegmentLength = 14

  val ExpectedPositionLength = 12

  def extractSegment(
      segmentNote: String,
      segmentDataNote: String,
  )(
      data: Seq[ByteString],
      request: LiveTrackRequest,
  )(using GeometryFactory): (Valid[Segment], Valid[SegmentData]) =
    var segmentFailures     = Seq.empty[FailureNote]
    var segmentDataFailures = Seq.empty[FailureNote]
    val coordinates         = Array.ofDim[Coordinate](data.size)
    val positionData        = Array.ofDim[PositionData](data.size)
    var now                 = GrpcZonedDateTimeProtocol(request.timestamp)
    var index               = 0
    var shouldRead          = false

    for item <- data do
      if item.size() == ExpectedSegmentLength then
        if shouldRead then
          val dataInput          = DataInputStream(item.newInput())
          val x                  = dataInput.readFloat()
          val y                  = dataInput.readFloat()
          val altitude           = dataInput.readUnsignedShort() - ZeroAltitude
          val timePack           = dataInput.readUnsignedShort()
          val horizontalAccuracy = dataInput.readUnsignedByte()
          val verticalAccuracy   = dataInput.readUnsignedByte()
          coordinates(index) = Coordinate(x, y, altitude.toDouble)

          now = now.plusSeconds(timePack >>> 6)
          positionData(index) = TransformTo[PositionData]((now, horizontalAccuracy, verticalAccuracy))
      else
        val message = s"Invalid data length of $index-th element."
        segmentFailures :+= FailureNote(segmentNote, message)
        segmentDataFailures :+= FailureNote(segmentDataNote, message)
        shouldRead = false
      index += 1

    if segmentFailures.isEmpty then
      (
        Right(TransformTo[Segment](summon[GeometryFactory].createLineString(coordinates))),
        Right(TransformTo[SegmentData](positionData.toSeq)),
      )
    else
      (
        Left(segmentFailures),
        Left(segmentDataFailures),
      )

  def extractPosition(
      notePosition: String,
      notePositionData: String,
  )(data: ByteString, request: LiveTrackRequest)(using GeometryFactory): (Valid[Position], Valid[PositionData]) =
    if data.size() == ExpectedPositionLength then
      val dataInput          = DataInputStream(data.newInput())
      val x                  = dataInput.readFloat()
      val y                  = dataInput.readFloat()
      val altitude           = dataInput.readUnsignedShort() - ZeroAltitude
      val horizontalAccuracy = HorizontalAccuracy(dataInput.readUnsignedByte())
      val verticalAccuracy   = VerticalAccuracy(dataInput.readUnsignedByte())
      val recordedAt         = RecordedAt(GrpcZonedDateTimeProtocol(request.timestamp))
      (
        Right(Position(summon[GeometryFactory].createPoint(Coordinate(x, y, altitude)))),
        Right(PositionData(recordedAt, horizontalAccuracy, verticalAccuracy)),
      )
    else
      (
        Left(Seq(FailureNote(notePosition, "Invalid data length."))),
        Left(Seq(FailureNote(notePositionData, "Invalid data length."))),
      )
