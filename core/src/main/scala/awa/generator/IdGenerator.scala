package awa.generator

import java.util.UUID

trait IdGenerator:

  def generate(): UUID

object IdGenerator extends IdGenerator:

  override def generate(): UUID = UUID.randomUUID()
