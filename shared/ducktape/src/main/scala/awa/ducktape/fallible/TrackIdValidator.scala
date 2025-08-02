package awa.ducktape.fallible

import awa.ducktape.TransformTo
import awa.model.data.TrackId
import awa.validation.FailureNote
import awa.validation.IsValid
import java.util.UUID
import scala.util.Try

object TrackIdValidator:

  def validate(note: String)(value: String): IsValid[TrackId] =
    for cause <- (
                   for uuid <- Try(UUID.fromString(value)) yield TransformTo[TrackId](uuid)
                 ).toEither.left
    yield Seq(FailureNote(note, cause.getMessage))
