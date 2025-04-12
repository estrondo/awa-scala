package awa.scalamock

import awa.generator.KeyGenerator
import org.scalamock.stubs.ZIOStubs

trait KeyGeneratorZIOStub:
  this: ZIOStubs =>

  inline def stubKeyGeneratorL4(key: String = KeyGenerator.generateL4()) =
    ScalaMockZIO.stubLayerWith[KeyGenerator].apply { generator =>
      (() => generator.generateL4()).returns(key)
      key
    }

  inline def stubKeyGeneratorL8(key: String = KeyGenerator.generateL8()) =
    ScalaMockZIO.stubLayerWith[KeyGenerator].apply { generator =>
      (() => generator.generateL8()).returns(key)
      key
    }

  inline def stubKeyGeneratorL16(key: String = KeyGenerator.generateL16()) =
    ScalaMockZIO.stubLayerWith[KeyGenerator].apply { generator =>
      (() => generator.generateL16()).returns(key)
      key
    }

  inline def stubKeyGeneratorL32(key: String = KeyGenerator.generateL32()) =
    ScalaMockZIO.stubLayerWith[KeyGenerator].apply { generator =>
      (() => generator.generateL32()).returns(key)
      key
    }

  inline def stubKeyGeneratorL64(key: String = KeyGenerator.generateL64()) =
    ScalaMockZIO.stubLayerWith[KeyGenerator].apply { generator =>
      (() => generator.generateL64()).returns(key)
      key
    }
