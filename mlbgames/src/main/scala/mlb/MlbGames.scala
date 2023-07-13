package mlb

import zio.*
import zio.jdbc.*
import zio.http.*

object MlbGames extends ZIOAppDefault {

  val createZIOPoolConfig: ULayer[ZConnectionPoolConfig] =
    ZLayer.succeed(ZConnectionPoolConfig.default)

  val properties: Map[String, String] = Map(
    "user" -> "postgres",
    "password" -> "postgres"
  )

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
