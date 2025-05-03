package awa.persistence.postgres

import awa.AwaException
import javax.sql.DataSource
import zio.stream.ZStream

type JdbcStream[A] = ZStream[DataSource, AwaException, A]
