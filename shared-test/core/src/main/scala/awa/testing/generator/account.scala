package awa.testing.generator

import awa.model.Account
import awa.model.data.CreatedAt
import zio.test.Gen

extension (gen: AwaGen)
  def randomAccount: Gen[Any, Account] =
    for
      id       <- gen.randomAccountId
      email    <- gen.randomEmail
      createAt <- gen.nowZonedDateTime
      provider <- gen.randomIdentityProvider
    yield Account(
      id = id,
      email = email,
      createdAt = CreatedAt(createAt),
      provider = provider,
    )
