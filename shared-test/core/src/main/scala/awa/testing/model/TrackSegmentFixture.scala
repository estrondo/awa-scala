package awa.testing.model

import awa.input.LiveTrackSegmentInput
import awa.model.TrackSegment
import awa.testing.Fixture
import awa.testing.data.LineStringDataFixture
import awa.testing.data.LineStringFixture
import awa.testing.data.TagMapFixture
import io.github.arainko.ducktape.*
import scala.util.Random

object TrackSegmentFixture extends Fixture:

  def createRandom(random: Random = Random()): TrackSegment =
    TrackSegment(
      id = createRandomString(10, random),
      track = TrackFixture.createRandom(random),
      startedAt = createZonedDateTime(),
      segment = LineStringFixture.createRandom(10, random),
      positionData = LineStringDataFixture.createRandom(10, random),
      tagMap = TagMapFixture.createRandom(5, random),
      order = Option.when(random.nextBoolean())(random.nextInt(10)),
    )

  def createFrom(input: LiveTrackSegmentInput, random: Random = Random()): TrackSegment =
    input
      .into[TrackSegment]
      .transform(
        Field.const(_.order, Option.when(random.nextBoolean())(random.nextInt(10))),
        Field.const(_.track, TrackFixture.createRandom(random)),
        Field.const(_.id, createRandomString(10, random)),
      )
