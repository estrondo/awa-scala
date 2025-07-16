package awa.geoindex

import awa.typeclass.TrackData

trait CellIndexer:

  def apply[T: TrackData](data: T): Seq[Cell]

object CellIndexer extends CellIndexer:

  val DefaultLevel = 13

  override def apply[T: TrackData](data: T): Seq[Cell] =
    TrackData[T]
      .foldLeft(data, Vector.empty[Cell]) { (cells, _, position) =>
        cells.lastOption match
          case Some(last) =>
            if last.contains(position) then cells
            else cells :+ Cell(position, DefaultLevel)
          case None       =>
            cells :+ Cell(position, DefaultLevel)
      }
