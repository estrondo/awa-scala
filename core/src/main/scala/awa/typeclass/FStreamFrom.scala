package awa.typeclass

import awa.FStream
import zio.stream.ZStream

trait FStreamFrom[S[_]]:

  def apply[A](input: S[A]): FStream[A]

object FStreamFrom:
  given FStreamFrom[Seq] with
    override def apply[A](input: Seq[A]): FStream[A] = ZStream.from(input)

  given FStreamFrom[List] with
    override def apply[A](input: List[A]): FStream[A] = ZStream.from(input)
