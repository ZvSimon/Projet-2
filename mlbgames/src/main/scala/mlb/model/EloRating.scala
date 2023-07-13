package mlb.model

import mlb.common.JDBC
import zio.ZIO
import zio.jdbc.{JdbcDecoder, JdbcEncoder, SqlFragment, ZConnectionPool, sqlInterpolator, stringToSql}
import zio.json.{DeriveJsonCodec, DeriveJsonDecoder, DeriveJsonEncoder, JsonCodec, JsonDecoder, JsonEncoder}
import zio.schema.Schema.Field
import zio.schema.{Schema, TypeId}

final case class EloRating(
                            gameId: String,
                            elo1_pre: String,
                            elo2_pre: String,
                            elo_prob1: String,
                            elo_prob2: String,
                            elo1_post: String,
                            elo2_post: String,
                          )

object EloRating extends JDBC[EloRating] {
  override val createTableQuery: String =
    s"""
       | CREATE TABLE IF NOT EXISTS EloRating (
       |      gameId VARCHAR NOT NULL,
       |      elo1_pre VARCHAR NOT NULL,
       |      elo2_pre VARCHAR NOT NULL,
       |      elo_prob1 VARCHAR NOT NULL,
       |      elo_prob2 VARCHAR NOT NULL,
       |      elo1_post VARCHAR NOT NULL,
       |      elo2_post VARCHAR NOT NULL
       | )
       |""".stripMargin
  implicit val schema: Schema[EloRating] =
    Schema.CaseClass7[String, String, String, String, String, String, String, EloRating](
      TypeId.parse(classOf[EloRating].getName),
      Field("gameId", Schema[String], get0 = _.gameId, set0 = (x, v) => x.copy(gameId = v)),
      Field("elo1_pre", Schema[String], get0 = _.elo1_pre, set0 = (x, v) => x.copy(elo1_pre = v)),
      Field("elo2_pre", Schema[String], get0 = _.elo2_pre, set0 = (x, v) => x.copy(elo2_pre = v)),
      Field("elo_prob1", Schema[String], get0 = _.elo_prob1, set0 = (x, v) => x.copy(elo_prob1 = v)),
      Field("elo_prob2", Schema[String], get0 = _.elo_prob2, set0 = (x, v) => x.copy(elo_prob2 = v)),
      Field("elo1_post", Schema[String], get0 = _.elo1_post, set0 = (x, v) => x.copy(elo1_post = v)),
      Field("elo2_post", Schema[String], get0 = _.elo2_post, set0 = (x, v) => x.copy(elo2_post = v)),
      EloRating.apply
    )
  implicit val jdbcDecoder: JdbcDecoder[EloRating] = JdbcDecoder.fromSchema
  implicit val jdbcEncoder: JdbcEncoder[EloRating] = JdbcEncoder.fromSchema
  implicit val encoder: JsonEncoder[EloRating] = DeriveJsonEncoder.gen[EloRating]
  implicit val decoder: JsonDecoder[EloRating] = DeriveJsonDecoder.gen[EloRating]
  implicit val codec: JsonCodec[EloRating] = DeriveJsonCodec.gen[EloRating]
  override def fromMap(map: Map[String, String]): EloRating = EloRating(
    map("gameId"),
    map("elo1_pre"),
    map("elo2_pre"),
    map("elo_prob1"),
    map("elo_prob2"),
    map("elo1_post"),
    map("elo2_post")
  )

  override val insertQuery: SqlFragment = sql"INSERT INTO EloRating(gameId,elo1_pre,elo2_pre,elo_prob1,elo_prob2,elo1_post,elo2_post)"
  override val selectAll: SqlFragment = sql"select * from EloRating"
}
