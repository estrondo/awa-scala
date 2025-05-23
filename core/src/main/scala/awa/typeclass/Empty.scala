package awa.typeclass

import scala.collection.MapFactory

trait Empty[A]:

  def empty: A

object Empty:

  given [K, V]: Empty[Map[K, V]] = newMapCanBeEmpty[K, V, Map](using Map)

  def newMapCanBeEmpty[K, V, M[_, _] <: Map[?, ?]](using factory: MapFactory[M]): Empty[M[K, V]] =
    new:
      override def empty: M[K, V] = factory.empty
