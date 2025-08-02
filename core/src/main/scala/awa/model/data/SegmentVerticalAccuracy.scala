package awa.model.data

opaque type SegmentVerticalAccuracy = Seq[Short]

object SegmentVerticalAccuracy:
  def apply(value: Seq[Short]): SegmentVerticalAccuracy = value
