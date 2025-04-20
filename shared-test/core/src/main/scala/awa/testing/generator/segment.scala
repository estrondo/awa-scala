package awa.testing.generator

import awa.model.data.Segment
import org.locationtech.jts.geom.LineString
import zio.test.Gen

extension (gen: AwaGen)
  def segment(sequence: Gen[Any, LineString]): Gen[Any, Segment] =
    for value <- sequence yield Segment(value)
