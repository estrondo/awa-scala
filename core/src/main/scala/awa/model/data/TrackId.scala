package awa.model.data

import awa.generator.KeyGenerator
import io.github.arainko.ducktape.Transformer

case class TrackId(value: String):
  require(value.nonEmpty)

object TrackId:

  given Transformer[TrackId, String] = _.value

  def apply(keyGenerator: KeyGenerator): TrackId = new TrackId(keyGenerator.generateL32())
