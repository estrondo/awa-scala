package awa.testing.generator

import awa.generator.KeyGenerator
import java.time.Clock
import java.time.ZonedDateTime
import java.time.temporal.ChronoUnit
import zio.ZIO
import zio.test.Gen

object AwaGen extends AwaGen

class AwaGen:

  val nowZonedDateTime: Gen[Any, ZonedDateTime] =
    Gen.fromZIO {
      ZIO.succeed {
        ZonedDateTime.now(Clock.systemUTC().getZone).truncatedTo(ChronoUnit.SECONDS)
      }
    }

  val keyGeneratorL16: Gen[Any, String] =
    Gen.fromZIO {
      ZIO.succeed {
        KeyGenerator.generateL16()
      }
    }

  val keyGeneratorL32: Gen[Any, String] =
    Gen.fromZIO {
      ZIO.succeed {
        KeyGenerator.generateL32()
      }
    }
