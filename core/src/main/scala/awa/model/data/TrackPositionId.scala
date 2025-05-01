package awa.model.data

opaque type TrackPositionId = String

object TrackPositionId:

  def apply(value: String): TrackPositionId = value

  extension (value: TrackPositionId) def value: String = value
