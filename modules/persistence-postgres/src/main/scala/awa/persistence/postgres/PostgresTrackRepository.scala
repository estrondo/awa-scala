package awa.persistence.postgres

import awa.AwaException
import awa.IO
import awa.mapErrorToAwa
import awa.model.Track
import awa.persistence.TrackRepository
import awa.persistence.postgres.row.TrackRow
import io.getquill.*
import io.github.arainko.ducktape.*
import javax.sql.DataSource
import zio.Task
import zio.ZIO
import zio.ZLayer

object PostgresTrackRepository:

  def apply(ctx: PostgresZioJdbcContext[SnakeCase], dataSource: IO[DataSource]): TrackRepository = new TrackRepository:

    private val layer = ZLayer(dataSource)

    import ctx.*

    private inline def table = quote {
      querySchema[TrackRow]("track")
    }

    override def add(track: Track): IO[Track] =
      for
        row <- convert(track)
        _   <- insert(row)
                 .mapErrorToAwa(AwaException.Unexpected("", _))
      yield track

    private def convert(track: Track): IO[TrackRow] =
      ZIO
        .attempt {
          track
            .into[TrackRow]
            .transform(
              Field.const(_.accountId, track.accountId.value),
            )
        }
        .mapErrorToAwa(AwaException.Conversion("Unable to convert to TrackRow!", _))

    private def insert(row: TrackRow): Task[Any] =
      run {
        quote {
          table.insertValue(lift(row))
        }
      }.provideLayer(layer)
