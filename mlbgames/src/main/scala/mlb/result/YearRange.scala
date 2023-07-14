package mlb.result

import zio.json.{DeriveJsonEncoder, JsonEncoder}

case class YearRange(start: Int, end: Int)

object YearRange {
  implicit val yearRangeEncoder: JsonEncoder[YearRange] = DeriveJsonEncoder.gen[YearRange]
}

