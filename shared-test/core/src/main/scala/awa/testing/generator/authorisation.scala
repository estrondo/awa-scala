package awa.testing.generator

import awa.model.Authorisation
import zio.test.Gen

extension (gen: AwaGen)
  def randomAuthorisation: Gen[Any, Authorisation] =
    for
      token     <- gen.randomToken
      accountId <- gen.randomAccountId
    yield Authorisation(token, accountId)
