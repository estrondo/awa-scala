package awa.model.data

import java.time.ZonedDateTime

opaque type SegmentTimestamp = Seq[ZonedDateTime]

object SegmentTimestamp:

  def apply(value: Seq[ZonedDateTime]): SegmentTimestamp = value
