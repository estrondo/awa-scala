package awa.test.testcontainers

import zio.RLayer
import zio.ZIO
import zio.ZLayer
import zio.kafka.consumer.ConsumerSettings
import zio.kafka.producer.ProducerSettings

object KafkaSettings:

  val producerSettings: RLayer[KafkaContainerWrapper, ProducerSettings] =
    ZLayer {
      for wrapper <- ZIO.service[KafkaContainerWrapper]
      yield ProducerSettings(List(wrapper.container.getBootstrapServers()))
        .withClientId("awa-shared-testing-producer")
    }

  val consumerSettings: RLayer[KafkaContainerWrapper, ConsumerSettings] =
    ZLayer {
      for wrapper <- ZIO.service[KafkaContainerWrapper]
      yield ConsumerSettings(List(wrapper.container.getBootstrapServers()))
        .withGroupId("awa-shared-testing-kafka")
        .withClientId("awa-shared-testing-consumer")
        .withProperty("auto.offset.reset", "earliest")
    }
