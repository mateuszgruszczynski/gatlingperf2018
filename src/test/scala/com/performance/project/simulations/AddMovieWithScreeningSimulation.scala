package com.performance.project.simulations

import com.performance.project.config.{CinemaProfiles, CinemaProtocols}
import com.performance.project.model.MovieDataProvider
import com.performance.project.steps.{MoviesSteps, ScreeningsSteps}
import io.gatling.core.Predef._

class AddMovieWithScreeningSimulation extends Simulation {

  val movieScenario = scenario("Add movie and screening")
    .exec(MoviesSteps.addMovieAndSaveId(MovieDataProvider.randomMovie, "movieId"))
    .exec(ScreeningsSteps.addMovieScreening("movieId"))

  setUp(
    movieScenario
      .inject(CinemaProfiles.defaultRampUp)
      .protocols(CinemaProtocols.loadBalancer)
  )
}