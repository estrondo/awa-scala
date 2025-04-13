package awa.testing.model

import awa.input.LiveTrackInput
import awa.model.Track
import awa.testing.Fixture
import io.github.arainko.ducktape.*
import scala.util.Random

object TrackFixture extends Fixture:

  def createRandom(random: Random = Random): Track =
    val id         = createRandomString(8, random)
    val account    = AccountFixture.createRandom(random)
    val startedAt  = createZonedDateTime()
    val deviceId   = Some(createRandomString(10, random))
    val deviceType = Some(createRandomString(6, random))
    val createdAt  = createZonedDateTime()

    Track(
      id = id,
      account = account,
      startedAt = startedAt,
      deviceId = deviceId,
      deviceType = deviceType,
      createdAt = createdAt,
    )

  def createFrom(input: LiveTrackInput, random: Random = Random()): Track =
    input
      .into[Track]
      .transform(
        Field.const(_.createdAt, createZonedDateTime()),
        Field.const(_.account, AccountFixture.createRandom(random)),
        Field.const(_.id, createRandomString(10, random)),
      )
