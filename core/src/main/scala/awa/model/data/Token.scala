package awa.model.data

opaque type Token = Array[Byte]

object Token:

  def apply(value: Array[Byte]): Token = value
