package awa.sbt

import java.io.File
import sbt.io.IO
import java.security.MessageDigest
import java.nio.charset.StandardCharsets
import java.util.concurrent.Future
import java.util.concurrent.ForkJoinPool
import java.util.concurrent.Callable
import org.postgresql.ds.PGSimpleDataSource
import org.flywaydb.core.Flyway
import org.flywaydb.core.api.Location
import java.net.Socket


object Migration extends LoggerHelper {
  
  def computeTag(location: File): String = {
    val md = MessageDigest.getInstance("MD5")

    for (file <- IO.listFiles(location, (file: File) => file.getName().endsWith(".sql")).sortBy(_.getName())) {
      md.update(file.getName().getBytes())
      md.update(IO.readBytes(file))
    }

    BigInt(md.digest()).abs.toString(16)
  }

  def start(location: File, port: Int)(implicit ctx: BuildContext): Future[Unit] = {
    ForkJoinPool
      .commonPool()
      .submit(new Callable[Unit] {
        override def call(): Unit = {

          var count = 0

          while (count < 100 && !Network.isOnline(port)) {
            Thread.sleep(100L)
            count += 1
          }

//          Thread.sleep(2000L)
          logger.info("🐘 Starting migration.")
          val dataSource = new PGSimpleDataSource()
          dataSource.setUser(ctx.username)
          dataSource.setPassword(ctx.password)
          dataSource.setDatabaseName(ctx.database)
          dataSource.setServerNames(Array("localhost"))
          dataSource.setPortNumbers(Array(port))

          Flyway.configure()
          .locations(s"filesystem:${location.getCanonicalPath()}")
          .dataSource(dataSource)
          .load()
          .migrate()

          logger.info("🐘 Migration has been completed.")
        }
      })
  }
}
