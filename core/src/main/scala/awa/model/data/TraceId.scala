package awa.model.data

import awa.typeclass.ToString
import io.github.arainko.ducktape.Transformer
import java.util.UUID

opaque type TraceId = UUID

object TraceId:

  def apply(value: UUID): TraceId = value

  extension (value: TraceId) def value: UUID = value

  given Transformer[UUID, TraceId] with
    override def transform(value: UUID): TraceId = value

  given ToString[TraceId] with
    override def toString(a: TraceId): String = a.toString()
