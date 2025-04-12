package awa.model

import awa.Fixture
import awa.model.Account

import scala.util.Random

object AccountFixture extends Fixture:

  def createRandom(random: Random = Random()): Account =
    val id        = createRandomString(8, random)
    val email     = s"${createRandomString(5, random)}@example.com"
    val createdAt = createZonedDateTime()
    val provider  = s"provider-${createRandomString(5, random)}"
    Account(
      id = id,
      email = email,
      createdAt = createdAt,
      provider = provider,
    )
