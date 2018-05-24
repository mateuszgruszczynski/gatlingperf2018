package com.performance.project.simulations

import com.performance.project.config.{CinemaAssertions, CinemaProfiles, CinemaProtocols}
import com.performance.project.model.MovieDataProvider
import com.performance.project.steps.MoviesSteps
import io.gatling.core.Predef._
import io.gatling.core.controller.inject.InjectionStep
import io.gatling.core.protocol.Protocol
import io.gatling.http.Predef.http
import scala.concurrent.duration._

class AddMovieSimulationViaLBHighLoad extends MovieSimulationTraits with ViaLoadBalancer with StandardAssertions with HighLoadOver5Minute

trait ViaLoadBalancer{
  def protocol: Protocol = http.baseURL("http://10.10.0.100:9000")
}

trait HighLoadOver5Minute{
  def ip: Seq[InjectionStep] = Seq(
    constantUsersPerSec(1).during(5 seconds)
  )
}

trait StandardAssertions{
  def assert = Seq(
    global.responseTime.max.lt(5000),
    global.responseTime.percentile4.lt(3000)
  )
}

abstract class MovieSimulationTraits extends Simulation{

  def ip: Seq[InjectionStep]      //
  def protocol: Protocol          // Deklaracja niezdefiniowanych metod (klasa abstrakcyjna może takie posiadać)
  def assert: Seq[Assertion]      //

  val scn = scenario("Add movie")
    .exec(MoviesSteps.addMovie(MovieDataProvider.randomMovie))  //
    .exec(MoviesSteps.getMovies)                                // Kroki z MoviesSteps zamknięte w bloki exec()
    .exec(MoviesSteps.searchMovieByTitlePart("abc"))            //

  setUp(
    scn.inject(ip)           // Profil z traita
      .protocols(protocol)   // Protokół z traita
  ).assertions(assert)       // Asercje z traita
}