package com.performance.project.simulations

import com.performance.project.model.UserDataProvider
import com.performance.project.steps.ExampleSteps
import io.gatling.core.Predef._
import io.gatling.http.Predef._
import scala.concurrent.duration._

class AuthUserSimulation extends Simulation {

  val scn = scenario("Add movie")
    .exec(ExampleSteps.addUserAndSaveRequestData(UserDataProvider.randomUser, "userData"))
    .exec(ExampleSteps.authenticateUserAndSaveToken("userData", "userToken"))

  setUp(
    scn
      .inject(constantUsersPerSec(2).during(10 second))
      .protocols(http.baseURL("http://10.10.0.100:9000"))
  )
}