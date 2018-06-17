package com.performance.project.simulations

import com.performance.project.config.{CinemaProfiles, CinemaProtocols}
import com.performance.project.model.UserDataProvider
import com.performance.project.steps.ExampleSteps
import io.gatling.core.Predef._

class AuthUserAndCheckIdSimulation extends Simulation {

  val userScenario = scenario("Add user and check id")
    .exec(ExampleSteps.addUserAndSaveRequestData(UserDataProvider.randomUser, "userData"))
    .exec(ExampleSteps.authenticateUserAndSaveToken("userData", "userToken", "userId"))
    .exec(ExampleSteps.getUserAndCheckId("userId", "userToken"))

  setUp(
    userScenario
      .inject(CinemaProfiles.defaultRampUp)
      .protocols(CinemaProtocols.loadBalancer)
  )
}