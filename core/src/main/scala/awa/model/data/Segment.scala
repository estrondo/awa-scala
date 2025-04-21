package awa.model.data

import io.github.arainko.ducktape.Transformer
import org.locationtech.jts.geom.LineString

case class Segment(value: LineString)

object Segment:

  given Transformer[LineString, Segment] with
    override def transform(value: LineString): Segment =
      Segment(value)
