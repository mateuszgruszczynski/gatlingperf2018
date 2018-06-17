package com.performance.project.config

import io.gatling.core.Predef.constantUsersPerSec
import scala.concurrent.duration._
import io.gatling.core.Predef._

object CinemaProfiles {
  val oneUser = atOnceUsers(1)

  val defaultRampUp = rampUsersPerSec(1).to(Config.baseLoad).during(Config.baseDuration seconds)

  val defaultConstant = constantUsersPerSec(Config.baseLoad).during(Config.baseDuration seconds)
}
