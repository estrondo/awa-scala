package awa.ducktape.fallible

import awa.model.data.DeviceId
import awa.validation.FailureNote
import awa.validation.Valid
import io.github.arainko.ducktape.Transformer

object DeviceIdValidator:

  def notEmpty(note: String, message: String)(
      value: String,
  )(using transformer: Transformer[String, DeviceId]): Valid[Option[DeviceId]] =
    if value.nonEmpty then Right(Some(transformer.transform(value)))
    else Left(Seq(FailureNote(note, message)))
