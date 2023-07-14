package mlb.result

import zio.json.{DeriveJsonEncoder, JsonEncoder}

case class TeamHistory(team: String, allMatches: List[Match], interval: YearRange)

object TeamHistory {
  implicit val listOfMapEncoder: JsonEncoder[TeamHistory] = DeriveJsonEncoder.gen[TeamHistory]
}