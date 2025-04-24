package awa.model.data

import io.github.arainko.ducktape.Transformer
import org.locationtech.jts.geom.LineString

opaque type Segment = LineString

object Segment:

  def apply(lineString: LineString): Segment = lineString

  given Transformer[LineString, Segment] with
    override def transform(value: LineString): Segment = value
