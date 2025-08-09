package awa.geoindex

import awa.typeclass.TrackData
import scala.collection.immutable.Queue

trait GeoIndexer:

  def apply[T: TrackData](data: T): Seq[IndexedGeoId]

object GeoIndexer extends GeoIndexer:

  val MaxLevel = 12

  override def apply[T: TrackData](data: T): Seq[IndexedGeoId] =

    val (head, last) = TrackData[T]
      .foldLeft(data)((Queue.empty[IndexedGeoId], Option.empty[IndexedGeoId])) {
        case (current @ (head, Some(previous)), index, position) =>
          val candidate = GeoId(position, MaxLevel)
          if previous.id.contains(candidate) then current
          else if candidate.contains(previous.id) then (head, Some(IndexedGeoId(index, candidate)))
          else (head :+ previous, Some(IndexedGeoId(index, candidate)))

        case ((head, None), index, position) =>
          (head, Some(IndexedGeoId(index, GeoId(position, MaxLevel))))
      }

    last match
      case Some(indexedGeoId) => head :+ indexedGeoId
      case None               => head
