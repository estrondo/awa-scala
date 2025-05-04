package awa.testing.generator

import awa.generator.IdGenerator
import java.time.Clock
import java.time.ZonedDateTime
import java.time.temporal.ChronoUnit
import java.util.UUID
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

  val generateId: Gen[Any, UUID] =
    Gen.fromZIO {
      ZIO.succeed(IdGenerator.generate())
    }
