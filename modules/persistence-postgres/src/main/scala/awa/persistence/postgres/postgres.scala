package awa.persistence.postgres

import awa.AwaException
import io.getquill.*
import java.sql.SQLException
import java.sql.Types
import javax.sql.DataSource
import org.locationtech.jts.geom.Geometry
import org.locationtech.jts.geom.GeometryFactory
import org.locationtech.jts.geom.Polygon
import org.locationtech.jts.io.WKBReader
import org.locationtech.jts.io.WKBWriter
import zio.stream.ZStream

type JdbcStream = [T] =>> ZStream[DataSource, AwaException, T]

inline def stIntersects(inline a: Geometry, inline b: Geometry): Boolean = quote {
  sql"ST_Intersects($a, $b)".as[Boolean]
}

trait PostgresCodecs[N <: NamingStrategy](val ctx: PostgresZioJdbcContext[N]):
  export ctx.*

  private def readWKB[G <: Geometry](bytes: Array[Byte]): G =
    WKBReader(GeometryFactory()).read(bytes).asInstanceOf[G]

  private def writeWKB(geometry: Geometry): Array[Byte] =
    WKBWriter(2, true).write(geometry)

  given Decoder[Polygon] = decoder { (i, row, _) =>
    row.getBytes(i) match
      case null  => null
      case bytes => readWKB(bytes)
  }

  given Encoder[Geometry] = encoder(
    Types.OTHER,
    (i, value, prepare) => {
      prepare.setBytes(i, writeWKB(value))
    },
  )

  given Decoder[Map[String, String]] = decoder { (idx, row, ses) =>
    row.getObject(idx)
    Map.empty
  }
