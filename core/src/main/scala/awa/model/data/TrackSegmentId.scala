package awa.model.data

import awa.generator.IdGenerator
import io.github.arainko.ducktape.Transformer
import java.util.UUID

opaque type TrackSegmentId = UUID

object TrackSegmentId:

  def apply(value: UUID): TrackSegmentId = value

  def apply(idGenerator: IdGenerator): TrackSegmentId = idGenerator.generate()

  given Transformer[TrackSegmentId, UUID] with
    override def transform(value: TrackSegmentId): UUID = value

  given Transformer[TrackSegmentId, String] with
    override def transform(value: TrackSegmentId): String = value.toString()

  extension (value: TrackSegmentId) def value: UUID = value
