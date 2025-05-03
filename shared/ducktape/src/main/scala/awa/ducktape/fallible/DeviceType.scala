package awa.ducktape.fallible

import awa.validation.FailureNote
import awa.model.data.DeviceType
import io.github.arainko.ducktape.Transformer
import awa.validation.Valid

object DeviceTypeValidator:

  def notEmpty(note: String, message: String)(
      value: String,
  )(using transform: Transformer[String, DeviceType]): Valid[Option[DeviceType]] =
    if value.nonEmpty then Right(Some(transform.transform(value)))
    else Left(Seq(FailureNote(note, message)))
