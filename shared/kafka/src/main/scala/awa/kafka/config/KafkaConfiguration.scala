package awa.kafka.config

import scala.collection.immutable.Map

case class KafkaConfiguration(
    consumer: Option[KafkaConfiguration.Consumer],
    producer: Option[KafkaConfiguration.Producer],
)

object KafkaConfiguration:

  case class Consumer(
      bootstrapServers: List[String],
      properties: Option[Map[String, String]],
      groupId: String,
  )

  case class Producer(
      bootstrapServers: List[String],
      properties: Option[Map[String, String]],
  )
