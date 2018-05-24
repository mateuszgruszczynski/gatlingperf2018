package com.performance.project.simulations

import com.performance.project.config.{CinemaAssertions, CinemaProtocols}
import com.performance.project.model.MovieDataProvider
import com.performance.project.steps.{MoviesSteps, ScreeningsSteps}
import io.gatling.core.Predef._

class AddMovieWithScreeningSimulation extends Simulation {

  val scn = scenario("Add movie and screening")
    .exec(MoviesSteps.addMovieAndSaveId(MovieDataProvider.randomMovie, "movieId")) // ten krok zapisuje id filmu do sesji pod podaną nazwą
    .exec(ScreeningsSteps.addMovieScreening("movieId")) // ten krok czyta id filmu z z sesji z podanego atrybutu

  import scala.concurrent.duration._

  setUp(scn.inject(constantUsersPerSec(0.1).during(30 seconds)).protocols(CinemaProtocols.loadBalancer)).assertions(CinemaAssertions.globalAssertions)
}