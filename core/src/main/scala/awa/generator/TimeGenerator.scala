package awa.generator

import java.time.Clock
import java.time.ZonedDateTime
import java.time.temporal.ChronoUnit

trait TimeGenerator:

  def now(): ZonedDateTime

object TimeGenerator:

  val UtcGenerator: TimeGenerator =
    val clock = Clock.systemUTC()
    new:
      override def now(): ZonedDateTime =
        ZonedDateTime.now(clock).truncatedTo(ChronoUnit.SECONDS)
