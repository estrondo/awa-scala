package awa.model.data

import io.github.arainko.ducktape.Transformer
import java.time.ZonedDateTime

case class StartedAt(value: ZonedDateTime)

object StartedAt:

  given Transformer[StartedAt, ZonedDateTime] = _.value

  given Transformer[ZonedDateTime, StartedAt] = StartedAt.apply
