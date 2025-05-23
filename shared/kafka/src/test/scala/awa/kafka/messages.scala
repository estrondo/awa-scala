package awa.kafka

import com.sksamuel.avro4s.AvroSchema
import com.sksamuel.avro4s.Decoder
import com.sksamuel.avro4s.Encoder
import java.util.UUID

final case class InputMessage(id: UUID, title: String, content: String)

object InputMessage:

  given Encoder[InputMessage] = Encoder.autoDerived

  given Decoder[InputMessage] = Decoder.autoDerived

  val avroSchema = AvroSchema[InputMessage]

final case class OutputMessage(id: UUID, content: String)

object OutputMessage:

  val avroSchema = AvroSchema[OutputMessage]

  given Encoder[OutputMessage] = Encoder.autoDerived

  given Decoder[OutputMessage] = Decoder.autoDerived
