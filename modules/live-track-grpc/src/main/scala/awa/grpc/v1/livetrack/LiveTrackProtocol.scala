package awa.grpc.v1.livetrack

import awa.generator.TimeGenerator
import awa.model.data.SegmentHorizontalAccuracy
import awa.model.data.SegmentPath
import awa.model.data.SegmentTimestamp
import awa.model.data.SegmentVerticalAccuracy
import awa.v1.generated.livetrack.LiveTrackRequest
import awa.validation.FailureNote
import awa.validation.IsValid
import com.google.protobuf.ByteString
import java.io.ByteArrayInputStream
import java.io.DataInputStream
import java.time.ZonedDateTime
import org.locationtech.jts.geom.Coordinate
import org.locationtech.jts.geom.GeometryFactory
import scala.annotation.tailrec

object LiveTrackProtocol:

  private val ZeroAltitude = 20000f

  type SegmentData =
    (
        IsValid[SegmentPath],
        IsValid[SegmentTimestamp],
        IsValid[SegmentHorizontalAccuracy],
        IsValid[SegmentVerticalAccuracy],
    )

  def extractTrackSegmentFromByteString(field: String, data: Seq[ByteString], request: LiveTrackRequest)(using
      GeometryFactory,
      TimeGenerator,
  ): SegmentData =
    extractTrackSegmentFromByteArray(field, data.map(_.toByteArray()), request)

  def extractTrackSegmentFromByteArray(field: String, data: Seq[Array[Byte]], request: LiveTrackRequest)(using
      GeometryFactory,
      TimeGenerator,
  ): SegmentData =
    val size = data.size
    if size > 1 then
      val coordinates        = Array.ofDim[Coordinate](size)
      val timestamp          = Vector.newBuilder[ZonedDateTime]
      val horizontalAccuracy = Vector.newBuilder[Short]
      val verticalAccuracy   = Vector.newBuilder[Short]

      @tailrec def readFully(index: Int, now: ZonedDateTime): SegmentData =
        if index < data.size then
          val bytes = data(index)
          if bytes.size != 14 then
            val failure = Left(Seq(FailureNote(field, s"The $index-th element must have 14 bytes.")))
            (
              failure,
              failure,
              failure,
              failure,
            )
          else
            val dataInput  = DataInputStream(ByteArrayInputStream(bytes))
            val x          = dataInput.readFloat()
            val y          = dataInput.readFloat()
            val z          = dataInput.readUnsignedShort() - ZeroAltitude
            val fragment   = dataInput.readUnsignedShort()
            val offset     = fragment >> 2
            val horizontal = dataInput.readUnsignedByte().toShort
            val vertical   = dataInput.readUnsignedByte().toShort

            val coordinate = Coordinate(x, y, z)
            val moment     = now.plusSeconds(offset)

            coordinates(index) = coordinate
            timestamp.addOne(moment)
            horizontalAccuracy.addOne(horizontal)
            verticalAccuracy.addOne(vertical)
            readFully(index + 1, moment)
        else
          (
            Right(SegmentPath(summon[GeometryFactory].createLineString(coordinates))),
            Right(SegmentTimestamp(timestamp.result())),
            Right(SegmentHorizontalAccuracy(horizontalAccuracy.result())),
            Right(SegmentVerticalAccuracy(verticalAccuracy.result())),
          )

      readFully(0, summon[TimeGenerator].of(request.timestamp))
    else
      val failures = Left(Seq(FailureNote(field, "Invalid data size, it was expected at least 2 elements.")))
      (
        failures,
        failures,
        failures,
        failures,
      )
