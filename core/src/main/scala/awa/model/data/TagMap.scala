package awa.model.data

import io.github.arainko.ducktape.Transformer

opaque type TagMap = Map[String, String]

object TagMap:
  def apply(value: Map[String, String]): TagMap             = value
  extension (tagMap: TagMap) def value: Map[String, String] = tagMap

  given Transformer[Map[String, String], TagMap] = TagMap.apply
