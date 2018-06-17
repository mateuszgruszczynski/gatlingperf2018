package com.performance.project.simulations

import com.performance.project.config.Config
import com.performance.project.model.{Example, MovieDataProvider, UserDataProvider}
import com.performance.project.steps.{ExampleSteps, MoviesSteps}
import io.gatling.core.Predef._
import io.gatling.core.controller.inject.InjectionStep
import io.gatling.core.protocol.Protocol
import io.gatling.http.Predef._

import scala.concurrent.duration._
import scala.reflect.io.File
import scala.util.Random

class AddMovieGetMoviesSimulation extends Simulation {

  //  val fj = jsonFile("src/main/resources/test.json")

  val movieScenario = scenario("Add movie")
    .feed(Example.exampleFeeder)
    .exec(http("Example").post("/movies").body(StringBody("${data}")).asJSON)
    .exec(MoviesSteps.addMovieAndSaveId(MovieDataProvider.randomMovie, "movieId"))
    .exec(MoviesSteps.getMoviesPage(Random.nextInt(50)))

  val exampleScenario =  scenario("Example")
    .exec(session => {
      session.set("timestamp", System.currentTimeMillis)
    })
    .exec(http("Send data")
      .post("http://example.com")
      .body(StringBody("""{"constant":123, "timestamp": ${timestamp}}"""))
      .asJSON
      .check(
        bodyString.transform(
          (body: String, session: Session) => {
            if(body.contains("success")){
              File("/path/to/file")
                .createFile()
                .appendAll(session("timestamp").as[String])
            }
          }
        )
      )
    )

  setUp(
    movieScenario
      .inject(rampUsersPerSec(5).to(10).during(3 seconds))
      .protocols(http.baseURL(Config.baseHost))
  ).assertions(Seq.empty[Assertion])

}