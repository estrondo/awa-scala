package awa.model.data

import io.github.arainko.ducktape.Transformer
import java.time.ZonedDateTime

case class CreatedAt(value: ZonedDateTime)

object CreatedAt:

  given Transformer[CreatedAt, ZonedDateTime] = _.value
