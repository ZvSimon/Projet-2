package mlb.model

import mlb.common.JDBC
import zio.jdbc.{JdbcDecoder, JdbcEncoder, SqlFragment, sqlInterpolator}
import zio.json.{DeriveJsonCodec, DeriveJsonDecoder, DeriveJsonEncoder, JsonCodec, JsonDecoder, JsonEncoder}
import zio.schema.Schema.Field
import zio.schema.{Schema, TypeId}

final case class Game(
                       gameID: String,
                       date: String,
                       season: String,
                       neutral: String,
                       playoff: String,
                       team1: String,
                       team2: String,
                       score1: String,
                       score2: String
                     )

object Game extends JDBC[Game] {
  override val createTableQuery: String =
    s"""
       | CREATE TABLE IF NOT EXISTS Game (
       |      gameId VARCHAR NOT NULL,
       |      date VARCHAR NOT NULL,
       |      season VARCHAR NOT NULL,
       |      neutral VARCHAR NOT NULL,
       |      playoff VARCHAR NOT NULL,
       |      team1 VARCHAR NOT NULL,
       |      team2 VARCHAR NOT NULL,
       |      score1 VARCHAR NOT NULL,
       |      score2 VARCHAR NOT NULL
       | )
       |""".stripMargin
  implicit val schema: Schema[Game] =
    Schema.CaseClass9[String, String, String, String, String, String, String, String, String, Game](
      TypeId.parse(classOf[Game].getName),
      Field("gameId", Schema[String], get0 = _.gameID, set0 = (x, v) => x.copy(gameID = v)),
      Field("date", Schema[String], get0 = _.date, set0 = (x, v) => x.copy(date = v)),
      Field("season", Schema[String], get0 = _.season, set0 = (x, v) => x.copy(season = v)),
      Field("neutral", Schema[String], get0 = _.neutral, set0 = (x, v) => x.copy(neutral = v)),
      Field("playoff", Schema[String], get0 = _.playoff, set0 = (x, v) => x.copy(playoff = v)),
      Field("team1", Schema[String], get0 = _.team1, set0 = (x, v) => x.copy(team1 = v)),
      Field("team2", Schema[String], get0 = _.team2, set0 = (x, v) => x.copy(team2 = v)),
      Field("score1", Schema[String], get0 = _.score1, set0 = (x, v) => x.copy(score1 = v)),
      Field("score2", Schema[String], get0 = _.score2, set0 = (x, v) => x.copy(score2 = v)),
      Game.apply
    )
  implicit val jdbcDecoder: JdbcDecoder[Game] = JdbcDecoder.fromSchema
  implicit val jdbcEncoder: JdbcEncoder[Game] = JdbcEncoder.fromSchema
  implicit val encoder: JsonEncoder[Game] = DeriveJsonEncoder.gen[Game]
  implicit val decoder: JsonDecoder[Game] = DeriveJsonDecoder.gen[Game]
  implicit val codec: JsonCodec[Game] = DeriveJsonCodec.gen[Game]

  override def fromMap(map: Map[String, String]): Game = Game(
    map("gameId"),
    map("date"),
    map("season"),
    map("neutral"),
    map("playoff"),
    map("team1"),
    map("team2"),
    map("score1"),
    map("score2")
  )

  override val insertQuery: SqlFragment = sql"INSERT INTO Game(gameId, date, season, neutral, playoff, team1, team2, score1, score2)"
  override val selectAll: SqlFragment = sql"SELECT * FROM Game"
}

