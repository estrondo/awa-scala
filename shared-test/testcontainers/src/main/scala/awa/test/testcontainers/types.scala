package awa.test.testcontainers

import org.testcontainers.containers.PostgreSQLContainer

trait ContainerWrapper:
  type Container <: org.testcontainers.containers.GenericContainer[?]

  def container: Container

//noinspection SpellCheckingInspection
class PostgreSQLContainerWrapper(override val container: org.testcontainers.containers.PostgreSQLContainer[?])
    extends ContainerWrapper:
  override type Container = org.testcontainers.containers.PostgreSQLContainer[?]

class KafkaContainerWrapper(override val container: org.testcontainers.containers.KafkaContainer)
    extends ContainerWrapper:
  override type Container = org.testcontainers.containers.KafkaContainer
