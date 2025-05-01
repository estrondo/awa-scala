package awa.model

import awa.model.data.AccountId
import awa.model.data.Token

case class Authorisation(
    token: Token,
    accountId: AccountId,
)
