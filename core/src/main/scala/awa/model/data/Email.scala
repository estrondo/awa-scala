package awa.model.data

opaque type Email = String

object Email:
  def apply(value: String): Email            = value
  extension (value: Email) def value: String = value
