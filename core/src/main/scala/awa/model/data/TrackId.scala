package awa.model.data

import awa.generator.IdGenerator
import awa.typeclass.ToString
import io.github.arainko.ducktape.Transformer
import java.util.UUID
import java.util as ju

opaque type TrackId = UUID

object TrackId:
  def apply(value: UUID): TrackId = value

  def apply(idGenerator: IdGenerator): TrackId = idGenerator.generate()

  extension (value: TrackId) def value: UUID = value

  given Transformer[TrackId, UUID] with
    override def transform(value: TrackId): UUID = value

  given Transformer[UUID, TrackId] with
    override def transform(value: UUID): TrackId = value

  given Transformer[TrackId, String] with
    override def transform(value: TrackId): String = value.toString()

  given ToString[TrackId] with
    override def toString(a: TrackId): String = a.toString()
