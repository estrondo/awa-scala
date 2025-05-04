package awa.typeclass

trait ToShow[A]:

  def show(a: A): String

object ToShow:
  inline def apply[A: ToShow as toShow](inline value: A): String =
    toShow.show(value)
