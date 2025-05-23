package awa.typeclass

import scala.collection.MapFactory

trait CanBeEmpty[A]:

  def empty: A

object CanBeEmpty:

  given [K, V, M[_, _] <: Map[?, ?]](using factory: MapFactory[M]): CanBeEmpty[M[K, V]] with
    override def empty: M[K, V] = factory.empty
