package awa.ducktape.fallible

import awa.typeclass.IsEmpty
import awa.validation.FailureNote
import awa.validation.Valid
import io.github.arainko.ducktape.Transformer

object NotEmptyValidator:

  def apply[Dest]: PartiallyApplied[Dest] = PartiallyApplied[Dest]()

  class PartiallyApplied[Dest]:
    def apply[Source](note: String, message: String)(
        value: Source,
    )(using t: Transformer[Source, Dest], e: IsEmpty[Source]): Valid[Dest] =
      if e.nonEmpty(value) then Right(t.transform(value))
      else Left(Seq(FailureNote(note, message)))
