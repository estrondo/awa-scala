package awa.model.data

import io.github.arainko.ducktape.Transformer

case class DeviceId(value: String)

object DeviceId:

  def apply(option: Option[String]): Option[DeviceId] =
    option match
      case Some(value) => Some(DeviceId(value))
      case None        => None

  given Transformer[DeviceId, String] = _.value
