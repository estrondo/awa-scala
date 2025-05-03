package awa.ducktape.fallible

import awa.ducktape.TransformTo
import awa.model.data.TrackId
import awa.validation.FailureNote
import awa.validation.Valid

object TrackIdValidator:

  private val regex = """^[a-zA-Z0-9]+$""".r

  def validate(note: String)(value: String): Valid[TrackId] =
    if regex.matches(value) then Right(TransformTo[TrackId](value))
    else Left(Seq(FailureNote(note, s"Invalid track id value: $value.")))
