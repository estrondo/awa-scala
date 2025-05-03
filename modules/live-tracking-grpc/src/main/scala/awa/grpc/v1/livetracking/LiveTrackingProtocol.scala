package awa.grpc.v1.livetracking

import awa.ducktape.TransformTo
import awa.grpc.protocol.GrpcZonedDateTimeProtocol
import awa.model.data.HorizontalAccuracy
import awa.model.data.Position
import awa.model.data.PositionData
import awa.model.data.RecordedAt
import awa.model.data.Segment
import awa.model.data.SegmentData
import awa.model.data.VerticalAccuracy
import awa.v1.livetrack.LiveTrackRequest
import awa.validation.FailureNote
import awa.validation.Valid
import com.google.protobuf.ByteString
import java.io.DataInputStream
import org.locationtech.jts.geom.Coordinate
import org.locationtech.jts.geom.GeometryFactory

object LiveTrackingProtocol:

  private val zeroAltitude = 20000

  def extractTrackSegmentData(
      noteSegment: String,
      noteSegmentData: String,
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
      if item.size() == 112 then
        if shouldRead then
          val dataInput          = DataInputStream(item.newInput())
          val x                  = dataInput.readFloat()
          val y                  = dataInput.readFloat()
          val altitude           = dataInput.readUnsignedShort() - zeroAltitude
          val bits               = dataInput.readInt()
          val timeOffset         = (bits >> 22) & 0x3ff
          val horizontalAccuracy = (bits >> 14) & 0xff
          val verticalAccuracy   = (bits >> 6) & 0xff
          now = now.plusSeconds(timeOffset)
          coordinates(index) = Coordinate(x, y, altitude.toDouble)
          positionData(index) = TransformTo[PositionData]((now, horizontalAccuracy, verticalAccuracy))
      else
        segmentFailures :+= FailureNote(noteSegment, s"Invalid data length of $index-th element.")
        segmentDataFailures :+= FailureNote(noteSegmentData, s"Invalid data length of $index-th element.")
        shouldRead = false
      index += 1

    if segmentFailures.nonEmpty then
      (
        Right(TransformTo[Segment](summon[GeometryFactory].createLineString(coordinates))),
        Right(TransformTo[SegmentData](positionData.toSeq)),
      )
    else
      (
        Left(segmentFailures),
        Left(segmentDataFailures),
      )

  def extractPositionData(
      notePosition: String,
      notePositionData: String,
  )(data: ByteString, request: LiveTrackRequest)(using GeometryFactory): (Valid[Position], Valid[PositionData]) =
    if data.size() == 128 then
      val dataInput          = DataInputStream(data.newInput())
      val x                  = dataInput.readFloat()
      val y                  = dataInput.readFloat()
      val altitude           = dataInput.readUnsignedShort() - zeroAltitude
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
