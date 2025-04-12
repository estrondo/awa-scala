package awa.scalamock

import org.scalamock.stubs.ZIOStubs
import zio.ZLayer

trait ZIOStubsLayer:
  this: ZIOStubs =>

  inline def stubLayer[T] = ZLayer.succeed {
    stub[T]
  }
