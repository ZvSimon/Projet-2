package mlb.common

import zio.jdbc.{Sql, SqlFragment, selectAll, transaction}
import zio.schema.Schema

trait JDBC[A] {
  implicit val schema:Schema[A]
  val createTableQuery:String
  val insertQuery:SqlFragment
  val selectAll:SqlFragment
  def fromMap(map: Map[String, String]):Product
}
