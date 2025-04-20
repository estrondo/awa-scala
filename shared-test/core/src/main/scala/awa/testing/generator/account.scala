package awa.testing.generator

import awa.model.Account
import zio.test.Gen

extension (gen: AwaGen)
  def randomAccount: Gen[Any, Account] =
    for
      id       <- AwaGen.keyGeneratorL16
      email    <- AwaGen.randomEmail
      createAt <- AwaGen.nowZonedDateTime
      provider <- Gen.stringN(10)(Gen.alphaNumericChar)
    yield Account(
      id = id,
      email = email,
      createdAt = createAt,
      provider = provider,
    )
