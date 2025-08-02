package awa.generator

import java.time.Clock
import java.time.Instant
import java.time.ZonedDateTime
import java.time.temporal.ChronoUnit

trait TimeGenerator:

  def now(): ZonedDateTime

  def of(timestamp: Long): ZonedDateTime

object TimeGenerator extends TimeGenerator:

  private val clock = Clock.systemUTC()

  override def now(): ZonedDateTime =
    ZonedDateTime.now(clock).truncatedTo(ChronoUnit.MILLIS)

  override def of(timestamp: Long): ZonedDateTime =
    ZonedDateTime.ofInstant(Instant.ofEpochSecond(timestamp), clock.getZone())
