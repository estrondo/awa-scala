package awa.model

import java.time.ZonedDateTime

case class Account(
    id: String,
    email: String,
    createdAt: ZonedDateTime,
    provider: String,
)
