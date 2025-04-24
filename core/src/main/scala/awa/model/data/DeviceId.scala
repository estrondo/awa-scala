package awa.model.data

import io.github.arainko.ducktape.Transformer

opaque type DeviceId = String

object DeviceId:

  def apply(value: String): DeviceId = value

  def apply(option: Option[String]): Option[DeviceId] =
    option match
      case Some(value) => Some(value)
      case None        => None

  given Transformer[DeviceId, String] with
    override def transform(value: DeviceId): String = value

  given Transformer[String, DeviceId] with
    override def transform(value: String): DeviceId = value
