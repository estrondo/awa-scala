package awa.ducktape

import io.github.arainko.ducktape.Transformer

//noinspection ScalaFileName
given [Source, Dest](using transformer: Transformer[Source, Dest]): Transformer[Seq[Source], Seq[Dest]] with
  override def transform(value: Seq[Source]): Seq[Dest] =
    for source <- value yield transformer.transform(source)
