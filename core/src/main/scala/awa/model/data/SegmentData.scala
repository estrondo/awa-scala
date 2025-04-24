package awa.model.data

import awa.input.PositionDataInput
import io.github.arainko.ducktape.Transformer

opaque type SegmentData = Seq[PositionData]

object SegmentData:

  def apply(value: Seq[PositionData]): SegmentData = value

  extension (value: SegmentData) def value: Seq[PositionData] = value

  given (using
      t: Transformer[PositionDataInput, PositionData],
  ): Transformer[Seq[PositionDataInput], SegmentData] with

    override def transform(value: Seq[PositionDataInput]): SegmentData =
      SegmentData(value.map(t.transform))
