package awa.model

import awa.model.data.AccountId
import awa.model.data.CreatedAt
import awa.model.data.Email
import awa.model.data.IdentityProvider

case class Account(
    id: AccountId,
    email: Email,
    createdAt: CreatedAt,
    provider: IdentityProvider,
)
