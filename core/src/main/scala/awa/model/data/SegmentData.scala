package awa.model.data

import io.github.arainko.ducktape.Transformer

opaque type SegmentData = Seq[PositionData]

object SegmentData:

  def apply(value: Seq[PositionData]): SegmentData = value

  extension (value: SegmentData) def value: Seq[PositionData] = value

  given [C[X] <: IterableOnce[X]]: Transformer[C[PositionData], SegmentData] with
    override def transform(value: C[PositionData]): SegmentData = value.toSeq
