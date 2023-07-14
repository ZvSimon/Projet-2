package mlb

import zio.*
import zio.jdbc.*
import zio.http.*
import zio.json.*
import zio.stream.ZStream 

import com.github.tototoshi.csv.*
import java.io.File

import mlb.query.*
import mlb.model.*

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

  // The createTables value is a ZIO effect that creates tables called "Game" , "EloRating","MlbPrediction","Pitcher" if it doesn't exist in the database.
  val createTables: ZIO[ZConnectionPool, Throwable, Unit] = transaction {
    execute(stringToSql(EloRating.createTableQuery))
      *> execute(stringToSql(Game.createTableQuery))
      *> execute(stringToSql(MlbPrediction.createTableQuery))
      *> execute(stringToSql(Pitcher.createTableQuery))
  }

  // insertRows execute a database transaction and perform an insert operation using the sqlFragment.
  def insertRows(sqlFragment: SqlFragment) = transaction {
    insert(sqlFragment)
  }

  val select: ZIO[ZConnectionPool, Throwable, Option[Int]] =
    transaction {
      selectOne(
        sql"SELECT COUNT(*) FROM Game".as[Int]
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
    _ <- Console.printLine("Starting app...")
    conn <- createTables
    _ <- Console.printLine("Tables are created...")
    source <- ZIO.succeed(CSVReader.open(new File(readFileCSV)))
    _ <- Console.printLine(s"Starting to ingest data from '$readFileCSV' file to tables from csv file.")
    _ <- ZStream
      .fromIterator[Map[String, String]](source.allWithOrderedHeaders()._2.iterator)
      .zipWithIndex.map { l => l._1.+("gameId" -> l._2.toString) }
      .grouped(1000)
      .map(_.toList)
      .foreach { list =>
        insertRows(EloRating.insertQuery.values[EloRating](list.map(EloRating.fromMap))) *>
          insertRows(Game.insertQuery.values[Game](list.map(Game.fromMap))) *>
          insertRows(MlbPrediction.insertQuery.values[MlbPrediction](list.map(MlbPrediction.fromMap))) *>
          insertRows(Pitcher.insertQuery.values[Pitcher](list.map(Pitcher.fromMap)))
      }
    _ <- Console.printLine("Finished ingesting data...")
    _ <- ZIO.succeed(source.close())
    _ <- Server.serve[ZConnectionPool](static ++ endpoints)
  } yield ()
 
  override def run: ZIO[Any, Throwable, Unit] =
    app.provide(createZIOPoolConfig >>> connectionPool, Server.default)
}
