package awa.kafka

import com.sksamuel.avro4s.AvroInputStream
import com.sksamuel.avro4s.Decoder
import org.apache.avro.Schema
import zio.kafka.consumer.CommittableRecord
import zio.stream.ZStream

extension [R, E, A](self: ZStream[R, E, A])
  def decode[T: Decoder](schema: Schema)(using
      ea: A <:< CommittableRecord[String, Array[Byte]],
      ee: Throwable <:< E,
  ): ZStream[R, E, T] =
    val builder = AvroInputStream.binary[T]
    self.flatMap { a =>
      val iterator = builder.from(ea(a).value).build(schema)
      ZStream.fromIterator(iterator.iterator).mapError(ee)
    }
