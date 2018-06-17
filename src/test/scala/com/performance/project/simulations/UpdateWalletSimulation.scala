package com.performance.project.simulations

import com.performance.project.config.{CinemaProfiles, CinemaProtocols, Config}
import com.performance.project.model.UserDataProvider
import com.performance.project.steps.ExampleSteps
import io.gatling.core.Predef._
import io.gatling.http.Predef._

class UpdateWalletSimulation extends Simulation {

  val walletScenario = scenario("Update wallet")
    .exec(ExampleSteps.addUserAndSaveRequestData(UserDataProvider.randomUser, "userData"))
    .exec(ExampleSteps.authenticateUserAndSaveToken("userData", "userToken", "userId"))
    .exec(ExampleSteps.getUserWalletValue("userId", "userToken", "userWalletValue"))
    .exec(ExampleSteps.payIntoUserWallet("userId", "userToken", 25.00))
    .exec(ExampleSteps.getUserWalletValue("userId", "userToken", "newUserWalletValue",
      jsonPath("$.value").ofType[Double].is(s => s("userWalletValue").as[Double] + 25.00)))

  setUp(
    walletScenario
      .inject(CinemaProfiles.defaultRampUp)
      .protocols(CinemaProtocols.loadBalancer)
  )
}
