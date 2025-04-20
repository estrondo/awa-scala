package awa.model.data

import io.github.arainko.ducktape.Transformer

case class TrackId(value: String):
  require(value.nonEmpty)

object TrackId:

  given Transformer[TrackId, String] = _.value
