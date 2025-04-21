package awa.testing.ducktape

import io.github.arainko.ducktape.Transformer

object DucktapeMap:

  def apply[Source, Dest](using t: Transformer[Source, Dest]): Source => Dest = t.transform
