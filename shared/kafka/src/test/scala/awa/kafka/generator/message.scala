package awa.kafka.generator

import awa.kafka.InputMessage
import awa.testing.generator.AwaGen
import zio.test.Gen

extension (self: AwaGen)
  def inputMessage: Gen[Any, InputMessage] =
    for
      uuid    <- Gen.uuid
      title   <- Gen.alphaNumericString
      content <- Gen.string
    yield InputMessage(
      id = uuid,
      title = title,
      content = content,
    )
