package mlb

import zio.*
import zio.jdbc.*
import zio.http.*
import zio.json.*

import com.github.tototoshi.csv.*
import java.io.File

object MlbGames extends ZIOAppDefault {

  // The createZIOPoolConfig value creates a ZIO layer that provides the configuration for the ZIO connection pool.
  val createZIOPoolConfig: ULayer[ZConnectionPoolConfig] =
    ZLayer.succeed(ZConnectionPoolConfig.default)

  // The properties value is a map that contains the database connection properties.
  val properties: Map[String, String] = Map(
    "user" -> "postgres",
    "password" -> "postgres"
  )

  // The connectionPool value creates a ZIO layer that represents the connection pool to the H2 in-memory database.
  val connectionPool : ZLayer[ZConnectionPoolConfig, Throwable, ZConnectionPool] =
    ZConnectionPool.h2mem(
      database = "testdb",
      props = properties
    )

  val create: ZIO[ZConnectionPool, Throwable, Unit] = transaction {
    execute(
      sql"CREATE TABLE IF NOT EXISTS games(date DATE NOT NULL, season_year INT NOT NULL, playoff_round INT)"
    )
  }

  val insertRows: ZIO[ZConnectionPool, Throwable, UpdateResult] = transaction {
    insert(
      sql"INSERT INTO games(date, season_year, playoff_round)"
        .values[(String, Int, Option[Int])](("2023-04-01", 2023, None), ("2023-04-01", 2023, None))
    )
  }

  val select: ZIO[ZConnectionPool, Throwable, Option[Int]] =
    transaction {
      selectOne(
        sql"SELECT COUNT(*) FROM games".as[Int]
      )
    }

  val static: App[Any] = Http.collect[Request] {
    case Method.GET -> Root / "text" => Response.text("Hello World!")
    case Method.GET -> Root / "json" => Response.json("""{"greetings": "Hello World!"}""")
  }

  // "getCSVHeader" function reads a CSV file specified by csvFile and returns a list of the ordered headers present in that file.
  def getCSVHeader(csvFile: String) = CSVReader.open(new File(csvFile)).allWithOrderedHeaders()._1

  // "selectAllAsList" executes the sqlFragment within a transaction,
  // retrieves multiple rows from the database using the selectAll operation, and returns the result as a list.
  def selectAllAsList[A](sqlFragment: Sql[A]) = transaction {
    selectAll(sqlFragment)
  }.map(_.toList)

  val readFileCSV = "mlb_elo.csv"

  val endpoints: App[ZConnectionPool] = Http.collectZIO[Request] {
    case Method.GET -> Root / "count" =>
      for {
        count: Option[Int] <- select
        res: Response = count match
          case Some(c) => Response.text(s"${c} game(s) in historical data")
          case None => Response.text("No game in historical data")
      } yield res
    case _ => ZIO.succeed(Response.text("Not found"))
  }.withDefaultErrorResponse

  val app: ZIO[ZConnectionPool & Server, Throwable, Unit] = for {
    conn <- create *> insertRows
    _ <- Server.serve[ZConnectionPool](static ++ endpoints)
  } yield ()

  override def run: ZIO[Any, Throwable, Unit] =
    app.provide(createZIOPoolConfig >>> connectionPool, Server.default)
}
