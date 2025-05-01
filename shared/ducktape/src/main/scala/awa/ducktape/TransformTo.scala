package awa.ducktape

import io.github.arainko.ducktape.Transformer

object TransformTo:

  def apply[Dest]: PartiallyAppliedTransform[Dest] = PartiallyAppliedTransform[Dest]()

  class PartiallyAppliedTransform[Dest]:
    def apply[Source](value: Source)(using transformer: Transformer[Source, Dest]): Dest =
      transformer.transform(value)
