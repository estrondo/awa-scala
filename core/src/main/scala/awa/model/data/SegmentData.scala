package awa.model.data

import awa.input.PositionDataInput
import io.github.arainko.ducktape.Transformer

case class SegmentData(value: Seq[PositionData])

object SegmentData:

  given (using
      t: Transformer[PositionDataInput, PositionData],
  ): Transformer[Seq[PositionDataInput], SegmentData] with

    override def transform(value: Seq[PositionDataInput]): SegmentData =
      SegmentData(value.map(t.transform))
