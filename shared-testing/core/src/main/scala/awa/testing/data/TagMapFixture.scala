package awa.testing.data

import awa.data.TagMap
import awa.testing.Fixture

import scala.util.Random

object TagMapFixture extends Fixture:

  def createRandom(size: Int, random: Random = Random): TagMap =
    Map.from {
      for _ <- 0 until size
      yield (createRandomString(5, random), createRandomString(10, random))
    }
