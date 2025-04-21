package awa.model.data

import awa.input.PositionDataInput
import io.github.arainko.ducktape.Transformer

case class PositionData(
    recordedAt: RecordedAt,
    horizontalAccuracy: HorizontalAccuracy,
    verticalAccuracy: VerticalAccuracy,
)

object PositionData:

  given Transformer[PositionDataInput, PositionData] with
    override def transform(value: PositionDataInput): PositionData =
      PositionData(
        recordedAt = RecordedAt(value._1),
        horizontalAccuracy = HorizontalAccuracy(value._2),
        verticalAccuracy = VerticalAccuracy(value._3),
      )
