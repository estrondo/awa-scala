package awa.kafka

import awa.AwaException
import com.sksamuel.avro4s.AvroInputStream
import com.sksamuel.avro4s.Decoder
import org.apache.avro.Schema

trait AvroReader[T]:

  def read(bytes: Array[Byte]): Either[AwaException.Decode, T]

object AvroReader:

  def apply[T: Decoder](schema: Schema): AvroReader[T] =
    new:

      private val builder = AvroInputStream.binary[T]

      override def read(bytes: Array[Byte]): Either[AwaException.Decode, T] =
        try
          val iterator = builder.from(bytes).build(schema).iterator
          if iterator.hasNext then Right(iterator.next())
          else Left(AwaException.Decode("Empty message, unable to read an Avro message."))
        catch case cause => Left(AwaException.Decode("Unable to read Avro message.", cause))
