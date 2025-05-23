package awa.typeclass

import java.time.ZonedDateTime

trait ToString[A]:

  def toString(a: A): String

object ToString:

  inline def apply[A: ToString as toShow](inline value: A): String =
    toShow.toString(value)

  given ToString[ZonedDateTime] with
    override def toString(a: ZonedDateTime): String = a.toString()
