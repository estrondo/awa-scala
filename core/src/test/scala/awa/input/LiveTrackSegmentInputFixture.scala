package awa.input

import awa.Fixture
import awa.data.LineStringFixture
import awa.data.PositionDataFixture
import awa.data.TagMapFixture
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
