package com.performance.project

import com.performance.project.simulations.{AddMovieSimulationBaseViaGateway, AddMovieSimulationViaLBHighLoad, AddMovieWithScreeningSimulation}
import io.gatling.app.Gatling
import io.gatling.core.config.{GatlingConfiguration, GatlingPropertiesBuilder}

object IDERunner extends App {

  val props = new GatlingPropertiesBuilder
  GatlingConfiguration
  props.sourcesDirectory("./src/main/scala")
  props.binariesDirectory("./target/classes")
  props.simulationClass(classOf[AddMovieSimulationBaseViaGateway].getName)
  Gatling.fromMap(props.build)
}