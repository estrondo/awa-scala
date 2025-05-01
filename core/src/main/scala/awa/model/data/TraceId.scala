package awa.model.data

import io.github.arainko.ducktape.Transformer

opaque type TraceId = String

object TraceId:

  def apply(value: String): TraceId = value

  given Transformer[String, TraceId] with
    override def transform(value: String): TraceId = value

  extension (value: TraceId) def value: String = value
