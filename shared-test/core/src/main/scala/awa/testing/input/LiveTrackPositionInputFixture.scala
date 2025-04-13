package awa.testing.input

import awa.input.LiveTrackPositionInput
import awa.input.LiveTrackPositionInput
import awa.testing.Fixture
import awa.testing.data.PositionDataFixture
import awa.testing.data.TagMapFixture

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
