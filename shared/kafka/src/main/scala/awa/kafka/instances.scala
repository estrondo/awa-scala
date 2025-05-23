package awa.kafka

import awa.typeclass.ToString
import zio.kafka.consumer.Offset

given ToString[Offset] with
  def toString(a: Offset): String = a.offset.toString()
