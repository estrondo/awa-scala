package awa.service

import awa.F
import awa.model.Account
import awa.model.data.AccountId

trait AccountService:

  def find(accountId: AccountId): F[Option[Account]]
