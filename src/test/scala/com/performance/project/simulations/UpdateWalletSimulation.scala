package com.performance.project.simulations

import com.performance.project.config.Config
import com.performance.project.model.UserDataProvider
import com.performance.project.steps.ExampleSteps
import io.gatling.core.Predef._
import io.gatling.http.Predef._

import scala.concurrent.duration._

class UpdateWalletSimulation extends Simulation {

  val myCookie = Cookie("example", "loremIpsum").withDomain("test.com").withMaxAge(3600)

  val walletScenario = scenario("Update wallet")
    .exec(ExampleSteps.addUserAndSaveRequestData(UserDataProvider.randomUser, "userData"))
    .exec(ExampleSteps.authenticateUserAndSaveToken("userData", "userToken", "userId"))
    .exec(ExampleSteps.getUserWalletValue("userId", "userToken", "userWalletValue"))
    .exec(ExampleSteps.payIntoUserWallet("userId", "userToken", 25.00))
    .exec(ExampleSteps.getUserWalletValue("userId", "userToken", "newUserWalletValue",
      jsonPath("$.value").ofType[Double].is(s => s("userWalletValue").as[Double] + 25.00)))

  setUp(
    walletScenario
      .inject(
        rampUsersPerSec(5).to(40).during(300 seconds),
//        constantUsersPerSec(10).during(240 seconds),
//        constantUsersPerSec(15).during(5 seconds),
//        constantUsersPerSec(10).during(240 seconds)
      )
      .protocols(http.baseURL(Config.baseHost))
  )

  //  val walletScenario = scenario("Update wallet")
  //    .exec(http("Get track").get("http://apprlb01a.ppr.azimo.com/service-tracking/v1/public/track"))
  //    .exec(http("Get health").get("http://apprlb01a.ppr.azimo.com/service-tracking/manage/health"))
  //  //    .exec(addCookie(myCookie))
  //  //    .exec(flushSessionCookies)
  //  //    .exec(flushCookieJar)
  //  //    .exec(flushHttpCache)
  //  //    //    //----------------------------//
  //  //    .exec(session => session.set("movieGenre", Random.shuffle(List("Crime", "Drama", "Fantasy")).head))
  //  //    .exec(ExampleSteps.getMoviesByGenre("movieGenre"))
  //  //    //.exec(ExampleSteps.addUserAndSaveRequestData(UserDataProvider.randomUser, "userData"))
  //  //    //    .exec(ExampleSteps.authenticateUserAndSaveToken("userData", "userToken", "userId"))
  //  //    //    .exec(ExampleSteps.getUserWalletValue("userId", "userToken", "userWalletValue"))
  //  //    //    .exec(ExampleSteps.payIntoUserWallet("userId", "userToken", 25.00))
  //  //    .exec(ExampleSteps.getUserWalletValue("userId", "userToken", "newUserWalletValue",
  //  //    jsonPath("$.value").ofType[Double].is(s => s("userWalletValue").as[Double] + 25.00)))
}
