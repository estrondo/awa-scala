package awa.model.data

import awa.generator.KeyGenerator

case class TrackSegmentId(value: String)

object TrackSegmentId:

  def apply(keyGenerator: KeyGenerator): TrackSegmentId =
    new TrackSegmentId(keyGenerator.generateL32())
