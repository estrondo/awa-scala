package awa.persistence.postgres

import awa.AwaException
import awa.F
import awa.L
import awa.mapErrorToAwa
import awa.model.Track
import awa.persistence.TrackRepository
import awa.persistence.postgres.row.TrackRow
import io.getquill.*
import io.github.arainko.ducktape.*
import javax.sql.DataSource
import zio.Task
import zio.ZIO

object PostgresTrackRepository:

  def apply(ctx: PostgresZioJdbcContext[SnakeCase], layer: L[DataSource]): TrackRepository = new TrackRepository:

    import ctx.*

    private inline def table = quote {
      querySchema[TrackRow]("track")
    }

    override def add(track: Track): F[Track] =
      for
        row <- convert(track)
        _   <- insert(row)
                 .mapErrorToAwa(AwaException.Unexpected("", _))
      yield track

    private def convert(track: Track): F[TrackRow] =
      ZIO
        .attempt {
          track
            .into[TrackRow]
            .transform(
              Field.const(_.accountId, track.accountId.value),
            )
        }
        .mapErrorToAwa(AwaException.Repository("Unable to convert to TrackRow!", _))

    private def insert(row: TrackRow): Task[Any] =
      run {
        quote {
          table.insertValue(lift(row))
        }
      }.provideLayer(layer)
