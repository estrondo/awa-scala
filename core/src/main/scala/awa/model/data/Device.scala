package awa.model.data

import io.github.arainko.ducktape.Transformer

opaque type Device = String

object Device:

  def apply(value: String): Device = value

  extension (value: Device) def value: String = value

  given Transformer[String, Device] with
    override def transform(value: String): Device = value

  given Transformer[Device, String] with
    override def transform(value: Device): String = value
