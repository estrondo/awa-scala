package awa.typeclass

import awa.model.TrackSegment
import awa.model.data.Position
import awa.model.data.SegmentPath

trait TrackData[T]:

  def foldLeft[V](data: T, initial: V)(f: (V, Int, Position) => V): V

object TrackData:

  inline def apply[T](using inline T: TrackData[T]): TrackData[T] = T

  given TrackData[TrackSegment] with

    override def foldLeft[V](data: TrackSegment, initial: V)(f: (V, Int, Position) => V): V =
      var outcome = initial
      for (index, position) <- data.path do outcome = f(outcome, index, position)
      outcome
