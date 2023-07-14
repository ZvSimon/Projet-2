package mlb.query

import zio.jdbc.*
import zio.json.*
import zio.schema.{Schema, TypeId}

final case class History(team: String,  startYear: Int, endYear: Int)

object History {
  // schema is an implicit Schema[History] that describes the schema of the History class.
  // It defines how the class fields map to the database schema.
  import Schema.Field
  implicit val schema: Schema[History] =
    Schema.CaseClass3[String, Int, Int, History](
      TypeId.parse(classOf[History].getName),
      Field("team", Schema[String], get0 = _.team, set0 = (x, v) => x.copy(team = v)),
      Field("startYear", Schema[Int], get0 = _.startYear, set0 = (x, v) => x.copy(startYear = v)),
      Field("endYear", Schema[Int], get0 = _.endYear, set0 = (x, v) => x.copy(endYear = v)),
      History.apply
    )

  // enable encoding and decoding History instances to and from database representations using JDBC.
  implicit val jdbcDecoder: JdbcDecoder[History] = JdbcDecoder.fromSchema
  implicit val jdbcEncoder: JdbcEncoder[History] = JdbcEncoder.fromSchema

  // enable encoding and decoding enable encoding and decoding History instances to and from JSON representations. instances to and from JSON representations.
  implicit val encoder: JsonEncoder[History] = DeriveJsonEncoder.gen[History]
  implicit val decoder: JsonDecoder[History] = DeriveJsonDecoder.gen[History]

  // combines the functionality of encoder and decoder into a single codec, allowing bidirectional encoding and decoding of History instances to and from JSON.
  implicit val codec: JsonCodec[History] = DeriveJsonCodec.gen[History]
}
