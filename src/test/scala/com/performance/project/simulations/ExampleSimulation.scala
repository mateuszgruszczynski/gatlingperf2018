package com.performance.project.simulations

import com.performance.project.model.UserDataProvider
import com.performance.project.steps.ExampleSteps
import io.gatling.http.Predef._
import io.gatling.core.Predef._

import scala.concurrent.duration._
import scala.util.Random

class ExampleSimulation extends Simulation {

  val myCookie = Cookie("example", "loremIpsum").withDomain("test.com").withMaxAge(3600)

  val exampleScenario = scenario("Examples")
    .exec(addCookie(myCookie))
    .exec(flushSessionCookies)
    .exec(flushCookieJar)
    .exec(flushHttpCache)
      .exec(ExampleSteps.getRandomMovieWithSE)

  setUp(
    exampleScenario
      .inject(constantUsersPerSec(3).during(30 seconds))
      .protocols(http.baseURL("http://192.168.88.223:9000"))
  )

}
