package awa.model.data

import io.github.arainko.ducktape.Transformer

opaque type Client = String

object Client:

  def apply(value: String): Client = value

  extension (value: Client) def value: String = value

  given Transformer[String, Client] with
    override def transform(value: String): Client = value

  given Transformer[Client, String] with
    override def transform(value: Client): String = value
