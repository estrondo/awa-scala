package awa.model.data

import awa.generator.KeyGenerator
import io.github.arainko.ducktape.Transformer

opaque type TrackSegmentId = String

object TrackSegmentId:

  def apply(value: String): TrackSegmentId = value

  def apply(keyGenerator: KeyGenerator): TrackSegmentId = keyGenerator.generateL32()

  given Transformer[TrackSegmentId, String] with
    override def transform(value: TrackSegmentId): String = value

  extension (value: TrackSegmentId) def value: String = value
