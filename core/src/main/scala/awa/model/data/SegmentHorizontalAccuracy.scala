package awa.model.data

opaque type SegmentHorizontalAccuracy = Seq[Short]

object SegmentHorizontalAccuracy:

  def apply(value: Seq[Short]): SegmentHorizontalAccuracy = value
