package mlb.model

final case class EloRating(
                            gameId: String,
                            elo1_pre: String,
                            elo2_pre: String,
                            elo_prob1: String,
                            elo_prob2: String,
                            elo1_post: String,
                            elo2_post: String,
                          )

