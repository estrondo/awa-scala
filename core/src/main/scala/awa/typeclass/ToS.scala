package awa.typeclass

import awa.S
import zio.stream.ZStream

trait ToS[C[_]]:

  def apply[A](input: C[A]): S[A]

object ToS:
  given ToS[Seq] with
    override def apply[A](input: Seq[A]): S[A] = ZStream.from(input)

  given ToS[List] with
    override def apply[A](input: List[A]): S[A] = ZStream.from(input)
