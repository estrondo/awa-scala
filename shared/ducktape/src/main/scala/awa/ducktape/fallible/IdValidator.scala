package awa.ducktape.fallible

import awa.validation.FailureNote
import awa.validation.IsValid
import io.github.arainko.ducktape.Transformer
import java.util.UUID
import scala.util.Try

object IdValidator:

  def apply[Dest](note: String)(value: String)(using t: Transformer[UUID, Dest]): IsValid[Dest] =
    apply(note, "Must be a valid UUID.")(value)

  def apply[Dest](note: String, message: String)(value: String)(using t: Transformer[UUID, Dest]): IsValid[Dest] =
    for exception <- Try(UUID.fromString(value))
                       .map(t.transform)
                       .toEither
                       .left
    yield Seq(FailureNote(note, message), FailureNote(note, exception.getMessage()))
