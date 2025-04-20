package awa.model.data

import io.github.arainko.ducktape.Transformer

case class DeviceType(value: String)

object DeviceType:

  def apply(option: Option[String]): Option[DeviceType] =
    option match
      case Some(value) => Some(DeviceType(value))
      case None        => None

  given Transformer[DeviceType, String] = _.value
