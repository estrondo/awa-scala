package awa.testing.generator

import awa.model.Authorisation
import zio.test.Gen

extension (self: AwaGen)
  def authorisation: Gen[Any, Authorisation] =
    for
      token     <- self.token
      accountId <- self.accountId
    yield Authorisation(token, accountId)
