package awa.model.data

import awa.generator.KeyGenerator

opaque type TrackSegmentId = String

object TrackSegmentId:

  def apply(value: String): TrackSegmentId = value

  def apply(keyGenerator: KeyGenerator): TrackSegmentId = keyGenerator.generateL32()

  extension (value: TrackSegmentId) def value: String = value
