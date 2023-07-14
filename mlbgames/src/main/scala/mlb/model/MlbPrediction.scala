package mlb.model

import mlb.common.JDBC
import zio.jdbc.{JdbcDecoder, JdbcEncoder, SqlFragment, sqlInterpolator}
import zio.json.{DeriveJsonCodec, DeriveJsonDecoder, DeriveJsonEncoder, JsonCodec, JsonDecoder, JsonEncoder}
import zio.schema.Schema.Field
import zio.schema.{Schema, TypeId}

final case class MlbPrediction(
                                gameId: String,
                                rating1_pre: String,
                                rating2_pre: String,
                                rating_prob1: String,
                                rating_prob2: String,
                                rating1_post: String,
                                rating2_post: String
                              )

object MlbPrediction extends JDBC[MlbPrediction] {
  override val createTableQuery: String =
    s"""
       | CREATE TABLE IF NOT EXISTS MlbPrediction (
       |      gameId VARCHAR NOT NULL,
       |      rating1_pre VARCHAR NOT NULL,
       |      rating2_pre VARCHAR NOT NULL,
       |      rating_prob1 VARCHAR NOT NULL,
       |      rating_prob2 VARCHAR NOT NULL,
       |      rating1_post VARCHAR NOT NULL,
       |      rating2_post VARCHAR NOT NULL
       | )
       |""".stripMargin
  implicit val schema: Schema[MlbPrediction] =
    Schema.CaseClass7[String, String, String, String, String, String, String, MlbPrediction](
      TypeId.parse(classOf[MlbPrediction].getName),
      Field("gameId", Schema[String], get0 = _.gameId, set0 = (x, v) => x.copy(gameId = v)),
      Field("rating1_pre", Schema[String], get0 = _.rating1_pre, set0 = (x, v) => x.copy(rating1_pre = v)),
      Field("rating2_pre", Schema[String], get0 = _.rating2_pre, set0 = (x, v) => x.copy(rating2_pre = v)),
      Field("rating_prob1", Schema[String], get0 = _.rating_prob1, set0 = (x, v) => x.copy(rating_prob1 = v)),
      Field("rating_prob2", Schema[String], get0 = _.rating_prob2, set0 = (x, v) => x.copy(rating_prob2 = v)),
      Field("rating1_post", Schema[String], get0 = _.rating1_post, set0 = (x, v) => x.copy(rating1_post = v)),
      Field("rating2_post", Schema[String], get0 = _.rating2_post, set0 = (x, v) => x.copy(rating2_post = v)),
      MlbPrediction.apply
    )
  implicit val jdbcDecoder: JdbcDecoder[MlbPrediction] = JdbcDecoder.fromSchema
  implicit val jdbcEncoder: JdbcEncoder[MlbPrediction] = JdbcEncoder.fromSchema
  implicit val encoder: JsonEncoder[MlbPrediction] = DeriveJsonEncoder.gen[MlbPrediction]
  implicit val decoder: JsonDecoder[MlbPrediction] = DeriveJsonDecoder.gen[MlbPrediction]
  implicit val codec: JsonCodec[MlbPrediction] = DeriveJsonCodec.gen[MlbPrediction]

  override def fromMap(map: Map[String, String]): MlbPrediction = MlbPrediction(
    map("gameId"),
    map("rating1_pre"),
    map("rating2_pre"),
    map("rating_prob1"),
    map("rating_prob2"),
    map("rating1_post"),
    map("rating2_post")
  )

  override val insertQuery: SqlFragment = sql"INSERT INTO MlbPrediction(gameId, rating1_pre, rating2_pre, rating_prob1, rating_prob2, rating1_post, rating2_post)"
  override val selectAll: SqlFragment = sql"SELECT * FROM MlbPrediction"
}
