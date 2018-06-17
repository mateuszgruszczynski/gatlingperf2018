package com.performance.project.simulations

import io.gatling.core.scenario.Simulation

import io.gatling.core.Predef._
import io.gatling.http.Predef._

class SimuSimulation extends Simulation {

  val scn = scenario("Test")
    .exec(
      http("get").get("http://192.168.88.223:9000/health")
        .check(jsonPath("$").saveAs("attr"))
    )
    .exec(
      session => {
        println(session("attr"))
        session
      }
    )

  setUp(scn.inject(constantUsersPerSec(1).during(5)))

}
