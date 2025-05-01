package awa.model.data

import awa.generator.KeyGenerator
import io.github.arainko.ducktape.Transformer

opaque type TrackId = String

object TrackId:
  def apply(value: String): TrackId = value

  def apply(keyGenerator: KeyGenerator): TrackId = keyGenerator.generateL32()

  extension (value: TrackId) def value: String = value

  given Transformer[TrackId, String] with
    override def transform(value: TrackId): String = value

  given Transformer[String, TrackId] with
    override def transform(value: String): TrackId = value
