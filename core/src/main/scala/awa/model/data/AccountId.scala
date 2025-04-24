package awa.model.data

opaque type AccountId = String

object AccountId:
  def apply(value: String): AccountId = value

  extension (value: AccountId) def value: String = value
