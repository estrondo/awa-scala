package awa.typeclass

import java.lang.Long as JLong
import java.time.ZonedDateTime

trait CompareTo[A, B]:

  def compareTo(a: A, b: B): Int

object CompareTo:

  given CompareTo[Long, ZonedDateTime] with
    override def compareTo(a: Long, b: ZonedDateTime): Int =
      JLong.compare(a, b.toEpochSecond)
