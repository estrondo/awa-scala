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

    def iterator: Iterator[(Int, Position)] =
      val limit = size
      Iterator.unfold(0) { index =>
        if index < limit then Some((index -> Position(self.getPointN(index)), index + 1))
        else None
      }
