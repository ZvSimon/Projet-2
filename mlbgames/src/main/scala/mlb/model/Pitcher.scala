package mlb.model

import mlb.common.JDBC
import zio.jdbc.{JdbcDecoder, JdbcEncoder, SqlFragment, sqlInterpolator}
import zio.json.{DeriveJsonCodec, DeriveJsonDecoder, DeriveJsonEncoder, JsonCodec, JsonDecoder, JsonEncoder}
import zio.schema.Schema.Field
import zio.schema.{Schema, TypeId}

final case class Pitcher(
                          gameId: String,
                          pitcher1: String,
                          pitcher2: String,
                          pitcher1_rgs: String,
                          pitcher2_rgs: String,
                          pitcher1_adj: String,
                          pitcher2_adj: String
                        )

object Pitcher extends JDBC[Pitcher] {
  override val createTableQuery: String =
    s"""
       | CREATE TABLE IF NOT EXISTS Pitcher (
       |      gameId VARCHAR NOT NULL,
       |      pitcher1 VARCHAR NOT NULL,
       |      pitcher2 VARCHAR NOT NULL,
       |      pitcher1_rgs VARCHAR NOT NULL,
       |      pitcher2_rgs VARCHAR NOT NULL,
       |      pitcher1_adj VARCHAR NOT NULL,
       |      pitcher2_adj VARCHAR NOT NULL
       | )
       |""".stripMargin
  implicit val schema: Schema[Pitcher] =
    Schema.CaseClass7[String, String, String, String, String, String, String, Pitcher](
      TypeId.parse(classOf[Pitcher].getName),
      Field("gameId", Schema[String], get0 = _.gameId, set0 = (x, v) => x.copy(gameId = v)),
      Field("pitcher1", Schema[String], get0 = _.pitcher1, set0 = (x, v) => x.copy(pitcher1 = v)),
      Field("pitcher2", Schema[String], get0 = _.pitcher2, set0 = (x, v) => x.copy(pitcher2 = v)),
      Field("pitcher1_rgs", Schema[String], get0 = _.pitcher1_rgs, set0 = (x, v) => x.copy(pitcher1_rgs = v)),
      Field("pitcher2_rgs", Schema[String], get0 = _.pitcher2_rgs, set0 = (x, v) => x.copy(pitcher2_rgs = v)),
      Field("pitcher1_adj", Schema[String], get0 = _.pitcher1_adj, set0 = (x, v) => x.copy(pitcher1_adj = v)),
      Field("pitcher2_adj", Schema[String], get0 = _.pitcher2_adj, set0 = (x, v) => x.copy(pitcher2_adj = v)),
      Pitcher.apply
    )
  implicit val jdbcDecoder: JdbcDecoder[Pitcher] = JdbcDecoder.fromSchema
  implicit val jdbcEncoder: JdbcEncoder[Pitcher] = JdbcEncoder.fromSchema
  implicit val encoder: JsonEncoder[Pitcher] = DeriveJsonEncoder.gen[Pitcher]
  implicit val decoder: JsonDecoder[Pitcher] = DeriveJsonDecoder.gen[Pitcher]
  implicit val codec: JsonCodec[Pitcher] = DeriveJsonCodec.gen[Pitcher]

  override def fromMap(map: Map[String, String]): Pitcher = Pitcher(
    map("gameId"),
    map("pitcher1"),
    map("pitcher2"),
    map("pitcher1_rgs"),
    map("pitcher2_rgs"),
    map("pitcher1_adj"),
    map("pitcher2_adj")
  )

  override val insertQuery: SqlFragment = sql"INSERT INTO Pitcher(gameId, pitcher1, pitcher2, pitcher1_rgs, pitcher2_rgs, pitcher1_adj, pitcher2_adj)"
  override val selectAll: SqlFragment = sql"SELECT * FROM Pitcher"
}
