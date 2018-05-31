package com.performance.project.simulations

import com.performance.project.model.UserDataProvider
import com.performance.project.steps.ExampleSteps
import io.gatling.http.Predef._
import io.gatling.core.Predef._

import scala.concurrent.duration._
import scala.util.Random

class ExampleSimulation extends Simulation {

  val exampleScenario = scenario("Examples")
      .exec(ExampleSteps.getRandomMovieWithSE)

  setUp(
    exampleScenario
      .inject(constantUsersPerSec(3).during(5 seconds))
      .protocols(http.baseURL("http://192.168.88.223:9000"))
  )

}
