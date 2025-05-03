package awa.persistence.postgres

import io.getquill.*
import java.sql.PreparedStatement
import java.sql.Types
import org.locationtech.jts.geom.Geometry
import org.locationtech.jts.geom.GeometryFactory
import org.locationtech.jts.geom.LineString
import org.locationtech.jts.geom.Polygon
import org.locationtech.jts.io.WKBReader
import org.locationtech.jts.io.WKBWriter

trait PostgresCodec[N <: NamingStrategy](
    protected val ctx: PostgresZioJdbcContext[N],
    protected val geometryFactory: GeometryFactory,
):
  import ctx.*
  export ctx.Decoder
  export ctx.Encoder

  private def readWKB[G <: Geometry](bytes: Array[Byte]): G =
    WKBReader(geometryFactory).read(bytes).asInstanceOf[G]

  given Decoder[Polygon] = decoder { (i, row, _) =>
    row.getBytes(i) match
      case null  => null
      case bytes => readWKB(bytes)
  }

  protected def writeWKB[T <: Geometry](index: Int, value: T, statement: PreparedStatement) =
    statement.setBytes(index, WKBWriter(3, true).write(value))

  given Encoder[LineString] = encoder[LineString](Types.OTHER, writeWKB)

  given Decoder[Map[String, String]] = decoder { (idx, row, ses) =>
    row.getObject(idx)
    Map.empty
  }
