package awa.model.data

import io.github.arainko.ducktape.Transformer
import java.time.Clock
import java.time.Instant
import java.time.ZonedDateTime

opaque type StartedAt = ZonedDateTime

object StartedAt:
  def apply(value: ZonedDateTime): StartedAt                = value
  extension (startedAt: StartedAt) def value: ZonedDateTime = startedAt

  given Transformer[StartedAt, ZonedDateTime] = _.value
  given Transformer[ZonedDateTime, StartedAt] = StartedAt.apply

  given Transformer[Long, StartedAt] with
    override def transform(value: Long): StartedAt =
      ZonedDateTime.ofInstant(Instant.ofEpochSecond(value), Clock.systemUTC().getZone)
