package awa.kafka

import awa.kafka.config.KafkaConfiguration
import awa.kafka.generator.inputMessage
import awa.test.testcontainers.Container
import awa.test.testcontainers.KafkaContainerWrapper
import awa.test.testcontainers.KafkaSettings
import awa.testing.Spec
import awa.testing.generator.AwaGen
import com.sksamuel.avro4s.AvroOutputStream
import com.sksamuel.avro4s.Decoder
import com.sksamuel.avro4s.Encoder
import java.io.ByteArrayOutputStream
import org.apache.avro.Schema
import zio.ZIO
import zio.ZLayer
import zio.durationInt
import zio.kafka.consumer.Consumer
import zio.kafka.consumer.ConsumerSettings
import zio.kafka.consumer.Subscription
import zio.kafka.producer.Producer
import zio.kafka.producer.ProducerSettings
import zio.kafka.serde.Serde
import zio.stream.ZStream
import zio.test.*

object KafkaClientSpec extends Spec:

  private val defaultAspects = TestAspect.samples(5) @@ TestAspect.timeout(30.seconds)

  private val randomKey =
    for value <- Gen.alphaNumericString yield s"a-random-key-$value"

  private val randomInputRecord = randomKey.zip(AwaGen.inputMessage)

  private val randomInputs = Gen.listOfBounded(1, 5)(randomInputRecord)

  private val layer =
    (Container.kafkaContainer >+> ZLayer {
      for wrapper <- ZIO.service[KafkaContainerWrapper]
      yield KafkaConfiguration(
        consumer = Some(
          KafkaConfiguration.Consumer(
            List(wrapper.container.getBootstrapServers()),
            properties = Some(
              Map(
                "auto.offset.reset" -> "earliest",
              ),
            ),
            groupId = "awa-shared-kafka",
          ),
        ),
        producer = Some(
          KafkaConfiguration.Producer(
            bootstrapServers = List(wrapper.container.getBootstrapServers()),
            properties = Some(
              Map("batch.size" -> "0"),
            ),
          ),
        ),
      )
    }) >>> (
      KafkaSettings.consumerSettings ++
        KafkaSettings.producerSettings ++
        ZLayer {
          ZIO.serviceWithZIO[KafkaConfiguration](KafkaClient.apply)
        }
    )

  private val randomTopic =
    Gen.fromIterable(LazyList.from(0).map(x => s"random-topic-$x"), x => ZStream.succeed(x))

  def consumeTopic[T: Decoder](topic: String, schema: Schema) =
    for consumer <- ZIO.serviceWithZIO[ConsumerSettings](Consumer.make).asSomeError
    yield consumer
      .plainStream(Subscription.topics(topic), Serde.string, Serde.byteArray)
      .decode(schema)

  def produceRecord[T: Encoder as encoder](topic: String, schema: Schema)(key: String, value: T) =
    val bytes  = ByteArrayOutputStream()
    val output = AvroOutputStream.binary(schema, encoder).to(bytes).build()

    output.write(value)
    output.flush()
    val byteArray = bytes.toByteArray()

    for
      settings <- ZIO.service[ProducerSettings]
      producer <- Producer.make(settings)
      _        <- producer.produce(
                    topic = topic,
                    key = key,
                    value = byteArray,
                    keySerializer = Serde.string,
                    valueSerializer = Serde.byteArray,
                  )
    yield value

  override def spec = suite(nameOf[KafkaClient])(
    test("It should produces messages.") {
      check {
        for
          topic  <- randomTopic
          inputs <- randomInputs
        yield (topic, inputs)
      } { (topic, inputs) =>
        for
          _           <- KafkaClient.produce(topic, InputMessage.avroSchema)(inputs).flatMap(_.runDrain)
          inputStream <- consumeTopic[InputMessage](topic, InputMessage.avroSchema)
          outputs     <- inputStream.take(inputs.size).runCollect
        yield assertTrue(
          outputs == inputs.map(_._2),
        )
      }
    }.provideSomeLayer(layer) @@ defaultAspects,
    test("It should listen to messages.") {
      check {
        for
          topic  <- randomTopic
          inputs <- randomInputs
        yield (topic, inputs)
      } { (topic, inputs) =>
        for
          _       <- ZIO.foreach(inputs) { (key, expectedMessage) =>
                       produceRecord(topic, InputMessage.avroSchema)(key, expectedMessage)
                     }
          stream  <- ZIO.serviceWith[KafkaClient](_.listenTo[InputMessage](topic, InputMessage.avroSchema))
          outputs <- stream.take(inputs.size).runCollect
        yield assertTrue(
          outputs == inputs,
        )
      }
    }.provideSomeLayer(layer) @@ defaultAspects,
    test("It should flow messages.") {

      def process(key: String, input: InputMessage): Seq[(String, OutputMessage)] =
        Seq(
          s"${input.id}-1" -> OutputMessage(id = input.id, content = s"1-${input.content}"),
          s"${input.id}-2" -> OutputMessage(id = input.id, content = s"2-${input.content}"),
        )

      check {
        for
          topic  <- randomTopic
          inputs <- randomInputs
        yield (s"i$topic", s"o$topic", inputs)
      } { (inputTopic, outputTopic, inputs) =>
        for
          _            <- ZIO.foreach(inputs) { (key, input) =>
                            produceRecord(inputTopic, InputMessage.avroSchema)(key, input)
                          }
          flowStream   <- ZIO.serviceWith[KafkaClient](x =>
                            x.flow(inputTopic, InputMessage.avroSchema)(outputTopic, OutputMessage.avroSchema)(process),
                          )
          _            <- flowStream.take(inputs.size * 2).runDrain
          outputStream <- consumeTopic[OutputMessage](outputTopic, OutputMessage.avroSchema)
          outputs      <- outputStream.take(inputs.size * 2).runCollect
        yield assertTrue(
          outputs == inputs.flatMap(process).map(_._2),
        )
      }
    }.provideSomeLayer(layer) @@ defaultAspects,
  ) @@ TestAspect.sequential
