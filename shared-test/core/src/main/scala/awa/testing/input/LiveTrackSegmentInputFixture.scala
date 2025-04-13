package awa.testing.input

import awa.input.LiveTrackSegmentInput
import awa.testing.Fixture
import awa.testing.data.LineStringFixture
import awa.testing.data.PositionDataFixture
import awa.testing.data.TagMapFixture
import scala.util.Random

object LiveTrackSegmentInputFixture extends Fixture:

  def createRandom(random: Random = Random): LiveTrackSegmentInput =
    LiveTrackSegmentInput(
      traceId = createRandomString(10, random),
      tagMap = TagMapFixture.createRandom(size = random.nextInt(10) + 1, random),
      startedAt = createZonedDateTime(),
      deviceId = if random.nextBoolean() then Some(createRandomString(8, random)) else None,
      deviceType = if random.nextBoolean() then Some(createRandomString(6, random)) else None,
      segment = LineStringFixture.createRandom(length = random.nextInt(10) + 2, random),
      positionData = (0 until random.nextInt(10) + 1).map(_ => PositionDataFixture.createRandom(random)),
    )
