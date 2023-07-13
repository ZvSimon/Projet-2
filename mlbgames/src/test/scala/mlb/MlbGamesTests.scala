package mlb

import munit.*
import zio.http.*

class MlbGamesTests extends munit.ZSuite {

  val appForStatic: App[Any] = mlb.MlbGames.static

  testZ("should be Success /text") {

    val req = Request.get(URL(Root / "text"))
    assertZ(appForStatic.runZIO(req).isSuccess)
  }

  testZ("should be Success /json") {

    val req = Request.get(URL(Root / "text"))
    assertZ(appForStatic.runZIO(req).isSuccess)
  }

  testZ("should be Fail") {
    val req = Request.get(URL(Root))
    assertZ(appForStatic.runZIO(req).isFailure)
  }
  
}
