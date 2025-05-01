package awa.model.data

import io.github.arainko.ducktape.Transformer

opaque type DeviceType = String

object DeviceType:

  def apply(value: String): DeviceType = value

  extension (value: DeviceType) def value: String = value

  def apply(option: Option[String]): Option[DeviceType] =
    option match
      case Some(value) => Some(value)
      case None        => None

  given Transformer[String, DeviceType] with
    override def transform(value: String): DeviceType = value

  given Transformer[DeviceType, String] with
    override def transform(value: DeviceType): String = value
