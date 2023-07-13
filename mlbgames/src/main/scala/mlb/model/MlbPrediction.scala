package mlb.model

final case class MlbPrediction(
                                gameId: String,
                                rating1_pre: String,
                                rating2_pre: String,
                                rating_prob1: String,
                                rating_prob2: String,
                                rating1_post: String,
                                rating2_post: String
                              )

