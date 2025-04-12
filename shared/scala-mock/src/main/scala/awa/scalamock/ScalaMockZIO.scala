package awa.scalamock

import org.scalamock.stubs.Stub
import zio.Tag
import zio.URIO
import zio.ZIO

class ZIOServiceWithPartiallyApplied[T: Tag] extends AnyVal:
  def apply[A](f: Stub[T] => A): URIO[T, A] =
    ZIO.serviceWith[T] { t =>
      f(t.asInstanceOf[Stub[T]])
    }

class ZIOServiceWithZIOPartiallyApplied[T: Tag] extends AnyVal {
  def apply[R, E, A](f: Stub[T] => ZIO[R, E, A]) =
    ZIO.serviceWithZIO[T] { t =>
      f(t.asInstanceOf[Stub[T]])
    }
}

object ScalaMockZIO:

  def stubLayerWith[T: Tag]: ZIOServiceWithPartiallyApplied[T] = new ZIOServiceWithPartiallyApplied[T]

  def stubLayerWithZIO[T: Tag]: ZIOServiceWithZIOPartiallyApplied[T] = new ZIOServiceWithZIOPartiallyApplied[T]
