package awa.scalamock

import org.scalamock.stubs.Stub
import org.scalamock.stubs.ZIOStubs
import zio.Tag
import zio.ULayer
import zio.URIO
import zio.ZIO
import zio.ZLayer

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

trait ZIOStubBaseOperations:
  this: ZIOStubs =>

  def stubLayerWith[T: Tag]: ZIOServiceWithPartiallyApplied[T] = new ZIOServiceWithPartiallyApplied[T]

  def stubLayerWithZIO[T: Tag]: ZIOServiceWithZIOPartiallyApplied[T] = new ZIOServiceWithZIOPartiallyApplied[T]

  inline def stubLayer[T: Tag]: ULayer[Stub[T]] = ZLayer.succeed {
    stub[T]
  }
