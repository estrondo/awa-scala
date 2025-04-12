package awa.input

import awa.Fixture
import awa.data.PositionDataFixture
import awa.data.TagMapFixture

import scala.util.Random

object LiveTrackPositionInputFixture extends Fixture:

  def createRandom(random: Random = Random()): LiveTrackPositionInput =
    LiveTrackPositionInput(
      traceId = createRandomString(15, random),
      tagMap = TagMapFixture.createRandom(5, random),
      startedAt = createZonedDateTime(),
      deviceId = Some(createRandomString(10, random)),
      deviceType = Some(createRandomString(8, random)),
      point = createPoint(random),
      positionData = PositionDataFixture.createRandom(random),
    )
