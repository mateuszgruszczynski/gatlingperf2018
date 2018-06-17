package com.performance.project

import com.performance.project.simulations._
import io.gatling.app.Gatling
import io.gatling.core.config.{GatlingConfiguration, GatlingPropertiesBuilder}

object IDERunner extends App {

  val props = new GatlingPropertiesBuilder
  GatlingConfiguration
  props.sourcesDirectory("./src/main/scala")
  props.binariesDirectory("./target/classes")
  //props.simulationClass(classOf[UpdateWalletSimulation].getName)
  props.simulationClass(classOf[AuthUserAndCheckIdSimulation].getName)
  Gatling.fromMap(props.build)
}