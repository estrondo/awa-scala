package awa.ducktape.fallible

import awa.input.validation.FailureNote
import awa.input.validation.Valid
import awa.typeclass.IsEmpty
import io.github.arainko.ducktape.Transformer

object NotEmptyValidator:

  def apply[Dest]: PartiallyApplied[Dest] = PartiallyApplied[Dest]()

  class PartiallyApplied[Dest]:
    def apply[Source](note: String, message: String)(
        value: Source,
    )(using t: Transformer[Source, Dest], e: IsEmpty[Source]): Valid[Dest] =
      if e.nonEmpty(value) then Right(t.transform(value))
      else Left(Seq(FailureNote(note, message)))
