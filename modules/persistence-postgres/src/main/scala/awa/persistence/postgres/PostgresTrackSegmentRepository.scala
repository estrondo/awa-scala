package awa.persistence.postgres

import awa.AwaException
import awa.F
import awa.L
import awa.ducktape.TransformTo
import awa.ducktape.given
import awa.generator.TimeGenerator
import awa.mapErrorToAwa
import awa.model.TrackSegment
import awa.persistence.TrackSegmentRepository
import awa.persistence.postgres.row.TrackSegmentRow
import io.getquill.*
import io.github.arainko.ducktape.*
import javax.sql.DataSource
import org.locationtech.jts.geom.GeometryFactory
import zio.ZIO

object PostgresTrackSegmentRepository:

  def apply(
      ctx: PostgresZioJdbcContext[SnakeCase],
      layer: L[DataSource],
      geometryFactory: GeometryFactory,
      timeGenerator: TimeGenerator,
  ): TrackSegmentRepository =
    import ctx.*

    new TrackSegmentRepository with PostgresCodec[SnakeCase](ctx, geometryFactory):
      private inline def table = quote {
        querySchema[TrackSegmentRow]("track_segment")
      }

      override def add(segment: TrackSegment): F[TrackSegment] =
        for
          row <- convertToRow(segment)
          _   <- insertRow(row)
        yield segment

      private def convertToRow(segment: TrackSegment): F[TrackSegmentRow] =
        ZIO.fromEither {
          val now = timeGenerator.now()
          segment
            .into[TrackSegmentRow]
            .fallible
            .transform(
              Field.const(_.createdAt, now),
              Field.const(_.trackId, segment.trackId.value),
              Field.const(_.ord, TransformTo[Option[Int]](segment.order)),
            )
            .left
            .map(AwaException.WithNotes("Unable to convert to TrackSegmentRow.", _))
        }

      private def insertRow(row: TrackSegmentRow): F[TrackSegmentRow] = (
        run {
          quote {
            table.insertValue(lift(row))
          }
        }.provideLayer(layer)
          .mapErrorToAwa(AwaException.InsertionFailure("Unable to insert track segment.", _))
      ) as row
