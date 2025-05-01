package awa.model.data

import io.github.arainko.ducktape.Transformer
import java.time.ZonedDateTime

opaque type CreatedAt = ZonedDateTime

object CreatedAt:

  def apply(value: ZonedDateTime): CreatedAt = value

  extension (value: CreatedAt) def value: ZonedDateTime = value

  given Transformer[CreatedAt, ZonedDateTime] with
    override def transform(value: CreatedAt): ZonedDateTime = value

  given Transformer[ZonedDateTime, CreatedAt] with
    override def transform(value: ZonedDateTime): CreatedAt = value

  given Transformer[CreatedAt, Long] with
    override def transform(value: CreatedAt): Long = value.toEpochSecond
