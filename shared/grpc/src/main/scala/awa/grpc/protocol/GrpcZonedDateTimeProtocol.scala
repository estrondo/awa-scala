package awa.grpc.protocol

import java.time.Clock
import java.time.Instant
import java.time.ZonedDateTime

object GrpcZonedDateTimeProtocol:

  private val zoneId = Clock.systemUTC().getZone

  def apply(timestamp: Long): ZonedDateTime =
    ZonedDateTime.ofInstant(Instant.ofEpochSecond(timestamp), zoneId)
