package awa.kafka

import awa.AwaException
import awa.RF
import awa.S
import awa.annotated
import awa.getOrEmpty
import awa.kafka.config.KafkaConfiguration
import awa.logWarningCause
import awa.typeclass.ToS
import awa.typeclass.ToString
import com.sksamuel.avro4s.Decoder
import com.sksamuel.avro4s.Encoder
import org.apache.avro.Schema
import zio.Cause
import zio.Scope
import zio.ZIO
import zio.kafka.consumer.Consumer
import zio.kafka.consumer.ConsumerSettings
import zio.kafka.consumer.Subscription
import zio.kafka.producer.Producer
import zio.kafka.producer.ProducerSettings
import zio.kafka.serde.Serde
import zio.kafka.serde.Serializer
import zio.stream.ZStream

trait KafkaClient:

  def consumer: Consumer

  def producer: Producer

  def listenTo[I: Decoder](topic: String, schema: Schema): S[(String, I)]

  def flow[I: Decoder, O: Encoder, C[_]: ToS](inputTopic: String, inputSchema: Schema)(
      outputTopic: String,
      outputSchema: Schema,
  )(f: (String, I) => C[(String, O)]): S[(String, O)]

  def produce[O: Encoder, C[_]: ToS](topic: String, schema: Schema)(
      stream: C[(String, O)],
  ): S[(String, O)]

object KafkaClient:

  val consumerClientId = "awa-consumer-client"
  val producerClientId = "awa-producer-client"

  def produce[O: Encoder, C[_]: ToS](topic: String, schema: Schema)(
      stream: C[(String, O)],
  ): RF[KafkaClient, S[(String, O)]] =
    ZIO.serviceWith[KafkaClient](_.produce(topic, schema)(stream))

  def apply(config: KafkaConfiguration): RF[Scope, KafkaClient] =
    for
      cachedConsumer <- makeConsumer(config)
      cachedProducer <- makeProducer(config)
    yield new:
      override def consumer: Consumer = cachedConsumer

      override def producer: Producer = cachedProducer

      override def listenTo[I: Decoder](topic: String, schema: Schema): S[(String, I)] =
        val reader = AvroReader[I](schema)

        cachedConsumer
          .plainStream(
            Subscription.topics(topic),
            keyDeserializer = Serde.string,
            valueDeserializer = Serde.byteArray,
          )
          .flatMap { record =>
            (reader.read(record.value) match
              case Right(value) =>
                ZStream.succeed((record.key, value))
              case Left(cause)  =>
                ZStream.logWarningCause("Unable to read a message.", Cause.fail(cause)) *> ZStream.empty
            ).annotated(
              "kafka.offset" -> ToString(record.offset),
              "kafka.key"    -> record.key,
            )
          }
          .mapError(AwaException.Kafka("Unable to listen to a topic.", _))
          .annotated(
            "kafka.topic" -> topic,
          )

      override def flow[I: Decoder, O: Encoder, C[_]: ToS](
          inputTopic: String,
          inputSchema: Schema,
      )(
          outputTopic: String,
          outputSchema: Schema,
      )(f: (String, I) => C[(String, O)]): S[(String, O)] =
        listenTo[I](inputTopic, inputSchema)
          .flatMap { (key, value) =>
            produce[O, C](outputTopic, outputSchema)(f(key, value))
          }

      override def produce[O: Encoder, C[_]: ToS as toS](topic: String, schema: Schema)(
          values: C[(String, O)],
      ): S[(String, O)] =
        val writer = AvroWriter[O](schema)
        toS(values)
          .mapZIO { case input @ (key, value) =>
            writer.write(value) match
              case Right(bytes) =>
                producer.produceAsync(
                  topic = topic,
                  key = key,
                  value = bytes,
                  keySerializer = Serializer.string,
                  valueSerializer = Serializer.byteArray,
                ) as input
              case Left(cause)  =>
                ZIO.fail(cause)
          }
          .mapError(AwaException.Kafka("Unable to produce values.", _))
          .annotated(
            "kafka.topic" -> topic,
          )

  private def makeConsumer(config: KafkaConfiguration) =
    config.consumer match
      case Some(config) =>
        val settings = ConsumerSettings(config.bootstrapServers)
          .withGroupId(config.groupId)
          .withProperties(config.properties.getOrEmpty)
          .withClientId(consumerClientId)

        Consumer
          .make(settings)
          .mapError(AwaException.Kafka("Unable to prepare Kafka consumer!", _))
      case None         =>
        ZIO.fail(AwaException.Kafka("There is no consumer configuration for Kafka."))

  private def makeProducer(config: KafkaConfiguration) =
    config.producer match
      case Some(config) =>
        val settings = ProducerSettings(config.bootstrapServers)
          .withProperties(config.properties.getOrEmpty)
          .withClientId(producerClientId)
        Producer
          .make(
            settings,
          )
          .mapError(AwaException.Kafka("Unable to prepare Kafka producer.", _))
      case None         =>
        ZIO.fail(AwaException.Kafka("There is no producer configuration for Kafka."))
