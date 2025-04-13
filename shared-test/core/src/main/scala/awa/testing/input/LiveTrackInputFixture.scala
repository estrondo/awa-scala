package awa.testing.input

import awa.input.LiveTrackInput
import awa.input.LiveTrackInput
import awa.testing.Fixture

import scala.util.Random

object LiveTrackInputFixture extends Fixture:

  def createRandom(random: Random = Random): LiveTrackInput =
    LiveTrackInput(
      traceId = createRandomString(10, random),
      startedAt = createZonedDateTime(),
      deviceId = createRandomString(5, random).asRandomOption(random),
      deviceType = createRandomString(5, random).asRandomOption(random),
    )
