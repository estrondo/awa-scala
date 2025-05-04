package awa.ducktape.fallible

import awa.validation.FailureNote
import awa.validation.Valid
import io.github.arainko.ducktape.Transformer
import java.util.UUID
import scala.util.Try

object IdValidator:

  def apply[Dest](note: String)(value: String)(using t: Transformer[UUID, Dest]): Valid[Dest] =
    apply(note, "Must be a valid UUID.")(value)

  def apply[Dest](note: String, message: String)(value: String)(using t: Transformer[UUID, Dest]): Valid[Dest] =
    for exception <- Try(UUID.fromString(value))
                       .map(t.transform)
                       .toEither
                       .left
    yield Seq(FailureNote(note, message), FailureNote(note, exception.getMessage()))
