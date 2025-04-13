package awa.testing.data

import awa.data.LineStringData
import awa.testing.Fixture

import scala.util.Random

object LineStringDataFixture extends Fixture:

  def createRandom(length: Int, random: Random = Random): LineStringData = {
    for _ <- 0 until length yield PositionDataFixture.createRandom(random)
  }
