package awa.model.data

import java.time.ZonedDateTime

opaque type RecordedAt = ZonedDateTime

object RecordedAt:
  def apply(value: ZonedDateTime): RecordedAt            = value
  extension (value: RecordedAt) def value: ZonedDateTime = value
