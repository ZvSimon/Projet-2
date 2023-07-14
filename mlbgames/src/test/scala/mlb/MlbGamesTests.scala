package mlb

import munit.*
import zio.http.*
import zio.jdbc.ZConnectionPool

class MlbGamesTests extends munit.ZSuite {

  val appForStatic: App[Any] = mlb.MlbGames.static
  val appForEndpoints: App[ZConnectionPool] = mlb.MlbGames.endpoints
  
  testZ("should be Success /text") {
    val req = Request.get(URL(Root / "text"))
    assertZ(appForStatic.runZIO(req).isSuccess)
  }

  testZ("should be Success /json") {
    val req = Request.get(URL(Root / "json"))
    assertZ(appForStatic.runZIO(req).isSuccess)
  }

  testZ("should be able to run Successfully Root/count") {
    val req = Request.get(URL(Root / "count"))
    assertZ(appForEndpoints.runHandler(req).isSuccess)
  }
  
  testZ("should be Fail") {
    val req = Request.get(URL(Root))
    assertZ(appForStatic.runZIO(req).isFailure)
  }

  val team1 = "ATL"
  val team2 = "NYM"
  testZ("should be able to run Successfully Root/predict/team1/team2") {
    val req = Request.get(URL(Root / "predict" / team1 / team2))
    assertZ(appForEndpoints.runHandler(req).isSuccess)
  }

  testZ("should be able to run Successfully Root/history/team1/team2") {
    val req = Request.get(URL(Root / "history" / team1 / team2))
    assertZ(appForEndpoints.runHandler(req).isSuccess)
  }

}
