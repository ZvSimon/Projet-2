package mlb.model

import zio.jdbc.*
import zio.json.*
import zio.schema.Schema.Field
import zio.schema.{Schema, TypeId}

final case class Games(
                       date: String,
                       season_year: String,
                       playoff_round: String
                     )

object Games {
  implicit val schema: Schema[Games] =
    Schema.CaseClass3[String, String, String, Games](
      TypeId.parse(classOf[Games].getName),
      Field("date", Schema[String], get0 = _.date, set0 = (x, v) => x.copy(date = v)),
      Field("season_year", Schema[String], get0 = _.season_year, set0 = (x, v) => x.copy(season_year = v)),
      Field("playoff_round", Schema[String], get0 = _.playoff_round, set0 = (x, v) => x.copy(playoff_round = v)),
      Games.apply
    )
  implicit val jdbcDecoder: JdbcDecoder[Games] = JdbcDecoder.fromSchema
  implicit val jdbcEncoder: JdbcEncoder[Games] = JdbcEncoder.fromSchema
  implicit val gameEncoder: JsonEncoder[Games] = DeriveJsonEncoder.gen[Games]
  implicit val gameDecoder: JsonDecoder[Games] = DeriveJsonDecoder.gen[Games]
  implicit val codec: JsonCodec[Games] = DeriveJsonCodec.gen[Games]
}
