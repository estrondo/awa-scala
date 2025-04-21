package awa.input

import java.time.ZonedDateTime

/** What is this?
  *
  * *RecordedAt
  *
  * *HorizontalAccuracy
  *
  * *VerticalAccuracy
  */
type PositionDataInput = (ZonedDateTime, Int, Int)
