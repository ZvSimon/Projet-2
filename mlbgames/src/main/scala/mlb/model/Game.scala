package mlb.model

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

