package awa.model.data

import io.github.arainko.ducktape.Transformer

opaque type Order = Int

object Order:

  def apply(value: Int): Order            = value
  
  extension (value: Order) def value: Int = value

  given Transformer[Order, Int] with

    override def transform(value: Order): Int = value
