package awa.testing.generator

import awa.model.Account
import awa.model.data.CreatedAt
import zio.test.Gen

extension (self: AwaGen)
  def account: Gen[Any, Account] =
    for
      id       <- self.accountId
      email    <- self.email
      createAt <- self.nowZonedDateTime
      provider <- self.identityProvider
    yield Account(
      id = id,
      email = email,
      createdAt = CreatedAt(createAt),
      provider = provider,
    )
