package awa.model.data

import io.github.arainko.ducktape.Transformer

case class TagMap(value: Map[String, String])

object TagMap:

  given Transformer[Map[String, String], TagMap] = TagMap.apply
