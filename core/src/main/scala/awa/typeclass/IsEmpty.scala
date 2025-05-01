package awa.typeclass

trait IsEmpty[A]:

  def nonEmpty(a: A): Boolean

object IsEmpty:

  given IsEmpty[String] with
    override def nonEmpty(a: String): Boolean = a.nonEmpty
