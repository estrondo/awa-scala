package awa.ducktape

import io.github.arainko.ducktape.Transformer

//noinspection ScalaFileName
given [Source, Dest](using transformer: Transformer[Source, Dest]): Transformer[Seq[Source], Seq[Dest]] with
  override def transform(value: Seq[Source]): Seq[Dest] =
    for source <- value yield transformer.transform(source)


given [Source, Dest](using t: Transformer[Source, Dest]): Transformer[Option[Source], Option[Dest]] with
  override def transform(value: Option[Source]): Option[Dest] = 
    value match
      case Some(value) => Some(t.transform(value))
      case None => None
