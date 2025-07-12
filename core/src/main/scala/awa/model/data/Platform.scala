package awa.model.data

import io.github.arainko.ducktape.Transformer

opaque type Platform = String

object Platform:

  def apply(value: String): Platform = value

  extension (value: Platform) def value: String = value

  given Transformer[String, Platform] with
    override def transform(value: String): Platform = value

  given Transformer[Platform, String] with
    override def transform(value: Platform): String = value
