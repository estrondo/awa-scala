package awa.model.data

opaque type IdentityProvider = String

object IdentityProvider:
  def apply(value: String): IdentityProvider            = value
  extension (value: IdentityProvider) def value: String = value
