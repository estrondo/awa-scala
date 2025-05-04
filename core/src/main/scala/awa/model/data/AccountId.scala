package awa.model.data

import awa.typeclass.ToShow
import io.github.arainko.ducktape.Transformer
import java.util.UUID

opaque type AccountId = UUID

object AccountId:
  def apply(value: UUID): AccountId = value

  extension (value: AccountId) def value: UUID = value

  given Transformer[AccountId, String] with
    override def transform(value: AccountId): String = value.toString()

  given ToShow[AccountId] with
    override def show(a: AccountId): String = a.toString()
