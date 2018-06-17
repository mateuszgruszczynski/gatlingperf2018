package com.performance.project.simulations

import com.performance.project.config.{CinemaAssertions, CinemaProfiles, CinemaProtocols}
import com.performance.project.steps.MoviesSteps
import io.gatling.core.Predef._
import io.gatling.core.protocol.Protocol
import io.gatling.http.Predef._

import scala.concurrent.duration._

class GetMoviesSimulationLB extends GetMoviesSimulation with LB

abstract class GetMoviesSimulation extends Simulation{

  val protocol: Protocol

  val scn = scenario("Get movie")
    .exec(
      MoviesSteps.getMovies
    )

  setUp(
    scn
      .inject(CinemaProfiles.constantWithWarmup(2, 10))
      .protocols(protocol)
  ).assertions(CinemaAssertions.globalAssertions)
}

trait LB extends Simulation {
  val protocol: Protocol = CinemaProtocols.loadBalancer
}

trait AssertionAAA extends Simulation {
  val assertion = Seq(global.responseTime.max.lt(4000))
}