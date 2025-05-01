package awa.service

import awa.IO
import awa.model.Account
import awa.model.data.AccountId

trait AccountService:

  def find(accountId: AccountId): IO[Option[Account]]
