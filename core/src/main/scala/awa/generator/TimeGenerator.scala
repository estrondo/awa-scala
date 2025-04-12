package awa.generator

import java.time.ZonedDateTime
import java.time.ZoneOffset

trait TimeGenerator:

  def now(): ZonedDateTime

object TimeGenerator:

  val utcGenerator: TimeGenerator =
    () => ZonedDateTime.now(ZoneOffset.UTC)
