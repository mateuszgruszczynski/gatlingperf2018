package com.performance.project.config

import io.gatling.core.Predef.constantUsersPerSec
import scala.concurrent.duration._
import io.gatling.core.Predef._

object CinemaProfiles {
  val oneDuringOne = constantUsersPerSec(1).during(1)

  def constantWithWarmup(users: Int, duration: Int) = Seq(
    rampUsersPerSec(1).to(users).during(5 seconds),
    constantUsersPerSec(users).during(duration second)
  )
}
