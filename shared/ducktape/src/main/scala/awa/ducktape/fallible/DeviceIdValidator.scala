package awa.ducktape.fallible

import awa.validation.FailureNote
import awa.model.data.DeviceId
import io.github.arainko.ducktape.Transformer
import awa.validation.Valid

object DeviceIdValidator:

  def notEmpty(note: String, message: String)(
      value: String,
  )(using transformer: Transformer[String, DeviceId]): Valid[Option[DeviceId]] =
    if value.nonEmpty then Right(Some(transformer.transform(value)))
    else Left(Seq(FailureNote(note, message)))
