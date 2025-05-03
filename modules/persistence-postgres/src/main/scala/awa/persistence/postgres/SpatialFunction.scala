package awa.persistence.postgres

import io.getquill.*
import org.locationtech.jts.geom.Geometry

trait SpatialFunction:

  protected inline def stIntersects(inline a: Geometry, inline b: Geometry): Boolean = quote {
    sql"ST_Intersects($a, $b)".as[Boolean]
  }
