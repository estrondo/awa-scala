package awa.scalamock

import awa.generator.IdGenerator
import java.util.UUID
import org.scalamock.stubs.ZIOStubs

trait IdGeneratorZIOStub:
  this: ZIOStubs & ZIOStubBaseOperations =>

  inline def stubIdGenerator(id: UUID = IdGenerator.generate()) =
    stubLayerWith[IdGenerator] { generator =>
      (() => generator.generate()).returns(id)
    }
