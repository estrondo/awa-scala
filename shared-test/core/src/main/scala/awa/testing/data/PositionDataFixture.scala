package awa.testing.data

import awa.data.PositionData
import awa.data.PositionData
import awa.testing.Fixture

import scala.util.Random

object PositionDataFixture extends Fixture:

  def createRandom(random: Random = Random()): PositionData =
    PositionData(
      altitude = random.nextInt(9000),
      recordedAt = createZonedDateTime(),
      horizontalAccuracy = random.nextInt(60),
      altitudeAccuracy = random.nextInt(60),
    )
