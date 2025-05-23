package awa.kafka

import awa.AwaException
import com.sksamuel.avro4s.AvroOutputStream
import com.sksamuel.avro4s.Encoder
import java.io.ByteArrayOutputStream
import org.apache.avro.Schema

trait AvroWriter[T]:

  def write(value: T): Either[AwaException.EncodeFailure, Array[Byte]]

object AvroWriter:

  def apply[T: Encoder as encoder](schema: Schema): AvroWriter[T] =
    val builder = AvroOutputStream.binary(schema, encoder)
    new:
      override def write(value: T): Either[AwaException.EncodeFailure, Array[Byte]] =
        try
          val buffer = ByteArrayOutputStream()
          val output = builder.to(buffer).build()
          output.write(value)
          output.flush()
          Right(buffer.toByteArray())
        catch case cause: Throwable => Left(AwaException.EncodeFailure("Unable to encode message.", cause))
