package com.performance.project.simulations

import com.performance.project.config.{CinemaAssertions, CinemaProfiles, CinemaProtocols, Feeders}
import com.performance.project.model.{AuthorizationRequest, MovieDataProvider, ScreeningDataProvider}
import com.performance.project.steps.{MoviesSteps, ScreeningsSteps, UserSteps}
import io.gatling.core.Predef._
import io.gatling.core.protocol.Protocol
import io.gatling.core.session.Session
import io.gatling.http.Predef._

import scala.concurrent.duration._
import scala.util.Random

class AuthUserSimulation extends Simulation {

  val scn = scenario("Add movie")
    //    .exec(MoviesSteps.addMovie(MovieDataProvider.randomMovie))
    //    .exec(ScreeningsSteps.addMovieScreening("movieId"))
    .exec(MoviesSteps.getMovieAndSaveTitleAs(1, "movieTitle"))
    .exec(MoviesSteps.searchMovieByTitle("movieTitle"))

  setUp(scn.inject(CinemaProfiles.constantWithWarmup(1,1)).protocols(CinemaProtocols.loadBalancer)).assertions(CinemaAssertions.slowAssertion)
}