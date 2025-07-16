package awa.model.data

import io.github.arainko.ducktape.Transformer
import org.locationtech.jts.geom.LineString

opaque type Segment = LineString

object Segment:

  def apply(self: LineString): Segment = self

  given Transformer[LineString, Segment] with
    override def transform(value: LineString): Segment = value

  given Transformer[Segment, LineString] with
    override def transform(value: Segment): LineString = value

  extension (self: Segment)

    inline def value: LineString = self

    inline def foreach(f: (Int, Position) => Unit): Unit =
      for i <- 0 until self.getNumPoints() do f(i, Position(self.getPointN(i)))
