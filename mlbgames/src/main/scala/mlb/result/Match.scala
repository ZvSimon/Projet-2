package mlb.result

import mlb.model.*
import zio.json.{DeriveJsonEncoder, JsonEncoder}
case class Match(game: Game, eloRating: EloRating, mlbPrediction: MlbPrediction, pitcher: Pitcher)

object Match {
  implicit val allMatchesEncoder: JsonEncoder[Match] = DeriveJsonEncoder.gen[Match]
}