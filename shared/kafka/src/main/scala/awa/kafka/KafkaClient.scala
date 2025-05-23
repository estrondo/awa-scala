package awa.kafka

import awa.AwaException
import awa.FStream
import awa.RF
import awa.annotated
import awa.getOrEmpty
import awa.kafka.config.KafkaConfiguration
import awa.logWarningCause
import awa.typeclass.CanBeEmpty
import awa.typeclass.FStreamFrom
import com.sksamuel.avro4s.Decoder
import com.sksamuel.avro4s.Encoder
import org.apache.avro.Schema
import scala.collection.MapFactory
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

  def listenTo[I: Decoder](topic: String, schema: Schema): FStream[(String, I)]

  def flow[I: Decoder, O: Encoder, S[_]: FStreamFrom](inputTopic: String, inputSchema: Schema)(
      outputTopic: String,
      outputSchema: Schema,
  )(f: (String, I) => S[(String, O)]): FStream[(String, O)]

  def produce[O: Encoder, S[_]: FStreamFrom](topic: String, schema: Schema)(
      stream: S[(String, O)],
  ): FStream[(String, O)]

object KafkaClient:

  val consumerClientId = "awa-consumer-client"
  val producerClientId = "awa-producer-client"

  def produce[O: Encoder, S[_]: FStreamFrom](topic: String, schema: Schema)(
      stream: S[(String, O)],
  ): RF[KafkaClient, FStream[(String, O)]] =
    ZIO.serviceWith[KafkaClient](_.produce(topic, schema)(stream))

  def apply(config: KafkaConfiguration): RF[Scope, KafkaClient] =
    for
      cachedConsumer <- makeConsumed(config)
      cachedProducer <- makeProducer(config)
    yield new:
      override def consumer: Consumer = cachedConsumer

      override def producer: Producer = cachedProducer

      override def listenTo[I: Decoder as decoder](topic: String, schema: Schema): FStream[(String, I)] =
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
              "kafka.offset" -> record.offset.offset.toString(),
              "kafka.key"    -> record.key,
            )
          }
          .mapError(AwaException.KafkaException("Unable to listen to a topic.", _))
          .annotated(
            "kafka.topic" -> topic,
          )

      override def flow[I: Decoder, O: Encoder, S[_]: FStreamFrom](
          inputTopic: String,
          inputSchema: Schema,
      )(
          outputTopic: String,
          outputSchema: Schema,
      )(f: (String, I) => S[(String, O)]): FStream[(String, O)] =
        listenTo[I](inputTopic, inputSchema)
          .flatMap { (key, value) =>
            produce[O, S](outputTopic, outputSchema)(f(key, value))
          }

      override def produce[O: Encoder, S[_]: FStreamFrom as streamFrom](topic: String, schema: Schema)(
          stream: S[(String, O)],
      ): FStream[(String, O)] =
        val writer = AvroWriter[O](schema)
        streamFrom(stream)
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
          .mapError(AwaException.KafkaException("Unable to produce values.", _))
          .annotated(
            "kafka.topic" -> topic,
          )

  private given MapFactory[Map] = Map

  private def makeConsumed(config: KafkaConfiguration) =
    config.consumer match
      case Some(config) =>
        val settings = ConsumerSettings(config.bootstrapServers)
          .withGroupId(config.groupId)
          .withProperties(config.properties.getOrEmpty)
          .withClientId(consumerClientId)

        Consumer
          .make(settings)
          .mapError(AwaException.InvalidState("Unable to prepare Kafka consumer!", _))
      case None         =>
        ZIO.fail(AwaException.InvalidState("There is no consumer configuration for Kafka."))

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
          .mapError(AwaException.InvalidState("Unable to prepare Kafka producer.", _))
      case None         =>
        ZIO.fail(AwaException.InvalidState("There is no producer configuration for Kafka."))
