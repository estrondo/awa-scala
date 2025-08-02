package awa.ducktape.fallible

import awa.model.data.StartedAt
import awa.typeclass.CompareTo
import awa.typeclass.ToString
import awa.validation.FailureNote
import awa.validation.IsValid
import io.github.arainko.ducktape.Transformer
import java.time.ZonedDateTime

object StartedAtValidator:

  def notAfter[Source](note: String, reference: ZonedDateTime)(
      source: Source,
  )(using Transformer[Source, StartedAt], CompareTo[Source, ZonedDateTime]): IsValid[StartedAt] =
    notAfter(note, s"It must took place before ${ToString(reference)}.", reference)(source)

  def notAfter[Source](note: String, description: String, reference: ZonedDateTime)(source: Source)(using
      transformer: Transformer[Source, StartedAt],
      compareTo: CompareTo[Source, ZonedDateTime],
  ): IsValid[StartedAt] =
    if compareTo.compareTo(source, reference) <= 0 then Right(transformer.transform(source))
    else Left(Seq(FailureNote(note, "description")))
