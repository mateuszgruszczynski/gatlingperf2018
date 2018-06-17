package com.performance.project.simulations

import com.performance.project.config.{CinemaProfiles, CinemaProtocols}
import com.performance.project.model.MovieDataProvider
import com.performance.project.steps.MoviesSteps
import io.gatling.core.Predef._

import scala.util.Random

class AddMovieGetMoviesSimulation extends Simulation {

  val movieScenario = scenario("Add movie")
    .exec(MoviesSteps.addMovieAndSaveId(MovieDataProvider.randomMovie, "movieId"))
    .exec(MoviesSteps.getMoviesPage(Random.nextInt(50)))

  setUp(
    movieScenario
      .inject(CinemaProfiles.defaultRampUp)
      .protocols(CinemaProtocols.loadBalancer)
  )

}