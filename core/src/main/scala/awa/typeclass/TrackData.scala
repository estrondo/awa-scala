package awa.typeclass

import awa.model.TrackSegment
import awa.model.data.Position
import awa.model.data.SegmentPath

trait TrackData[A]:

  def foldLeft[B](data: A)(initial: => B)(f: (B, Int, Position) => B): B

object TrackData:

  inline def apply[A](using inline A: TrackData[A]): TrackData[A] = A

  given TrackData[TrackSegment] with

    override def foldLeft[B](data: TrackSegment)(initial: => B)(f: (B, Int, Position) => B): B =
      var current  = initial
      val iterator = data.path.iterator

      while iterator.hasNext do
        val (index, position) = iterator.next()
        current = f(current, index, position)

      current
