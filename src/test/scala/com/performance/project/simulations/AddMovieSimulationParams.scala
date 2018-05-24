package com.performance.project.simulations

import com.performance.project.config.{CinemaAssertions, CinemaProfiles, CinemaProtocols}
import com.performance.project.model.MovieDataProvider
import com.performance.project.steps.MoviesSteps
import io.gatling.core.Predef._
import io.gatling.core.controller.inject.InjectionStep
import io.gatling.core.protocol.Protocol
import io.gatling.http.Predef.http
import scala.concurrent.duration._

class AddMovieSimulationBaseViaLB extends MovieSimulationParam(
  CinemaProfiles.constantWithWarmup(10, 60),
  CinemaProtocols.loadBalancer,
  CinemaAssertions.slowAssertion
)

class AddMovieSimulationBaseViaGateway extends MovieSimulationParam(
  CinemaProfiles.constantWithWarmup(10, 60),
  CinemaProtocols.gateway,
  CinemaAssertions.slowAssertion
)

class AddMovieSimulationBaseViaGatewayFast extends MovieSimulationParam(
  CinemaProfiles.constantWithWarmup(5, 60),
  CinemaProtocols.gateway,
  CinemaAssertions.globalAssertions
)

class AddMovieSimulationBaseViaGatewayHighLoad extends MovieSimulationParam(
  CinemaProfiles.constantWithWarmup(50, 60),
  CinemaProtocols.gateway,
  CinemaAssertions.globalAssertions
)

abstract class MovieSimulationParam(ip: Seq[InjectionStep], prot: Protocol, assert: Seq[Assertion]) extends Simulation{
  val scn = scenario("Add movie")
    .exec(MoviesSteps.addMovie(MovieDataProvider.randomMovie))  //
    .exec(MoviesSteps.getMovies)                                // Kroki z MoviesSteps zamknięte w bloki exec()
    .exec(MoviesSteps.searchMovieByTitlePart("abc"))            //

  setUp(
    scn
      .inject(CinemaProfiles.constantWithWarmup(5, 120))        // Profil z obiektu CinemaProfiles
      .protocols(CinemaProtocols.loadBalancer)                  // Protokół z obiektu CinemaProtocols
  ).assertions(CinemaAssertions.scaledAssertions(2.5))          // Asercje z obiektu CinemaAssertions
}