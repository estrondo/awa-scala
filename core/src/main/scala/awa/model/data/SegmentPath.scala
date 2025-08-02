package awa.model.data

import io.github.arainko.ducktape.Transformer
import org.locationtech.jts.geom.LineString

opaque type SegmentPath = LineString

object SegmentPath:

  def apply(self: LineString): SegmentPath = self

  given Transformer[LineString, SegmentPath] with
    override def transform(value: LineString): SegmentPath = value

  given Transformer[SegmentPath, LineString] with
    override def transform(value: SegmentPath): LineString = value

  extension (self: SegmentPath)

    def size: Int = self.getNumPoints()

    inline def value: LineString = self

    inline def foreach(f: (Int, Position) => Unit): Unit =
      for i <- 0 until self.getNumPoints() do f(i, Position(self.getPointN(i)))
