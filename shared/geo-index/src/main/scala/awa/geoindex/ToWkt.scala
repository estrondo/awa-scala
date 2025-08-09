package awa.geoindex

import org.locationtech.jts.geom.Envelope
import org.locationtech.jts.geom.GeometryFactory

trait ToWkt[A]:

  def toWkt(a: A): String

object ToWkt:

  inline def apply[A](using inline t: ToWkt[A]): ToWkt[A] = t

  def toWkt[A: ToWkt](a: A): String = ToWkt[A].toWkt(a)

  given ToWkt[GeoCell] with

    override def toWkt(a: GeoCell): String =
      val rect    = a.value.getRectBound()
      val factory = GeometryFactory()
      factory
        .toGeometry(
          Envelope(
            rect.lngLo().degrees(),
            rect.lngHi().degrees(),
            rect.latLo().degrees(),
            rect.latHi().degrees(),
          ),
        )
        .toText()
