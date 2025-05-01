package awa.model.data

import io.github.arainko.ducktape.Transformer
import java.time.ZonedDateTime

case class PositionData(
    recordedAt: RecordedAt,
    horizontalAccuracy: HorizontalAccuracy,
    verticalAccuracy: VerticalAccuracy,
)

object PositionData:

  given single: Transformer[(ZonedDateTime, Int, Int), PositionData] with
    override def transform(value: (ZonedDateTime, Int, Int)): PositionData =
      PositionData(
        recordedAt = RecordedAt(value._1),
        horizontalAccuracy = HorizontalAccuracy(value._2),
        verticalAccuracy = VerticalAccuracy(value._3),
      )
