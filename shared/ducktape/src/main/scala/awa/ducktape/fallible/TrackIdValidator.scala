package awa.ducktape.fallible

import awa.ducktape.TransformTo
import awa.input.validation.FailureNote
import awa.input.validation.Valid
import awa.model.data.TrackId

object TrackIdValidator:

  private val regex = """^[a-zA-Z0-9]+$""".r

  def validate(note: String)(value: String): Valid[TrackId] =
    if regex.matches(value) then 
      Right(TransformTo[TrackId](value))
    else 
      Left(Seq(FailureNote(note, s"Invalid value: $value.")))
