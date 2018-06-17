package com.performance.project.config

import io.gatling.core.Predef._
import io.gatling.http.Predef._

object CinemaAssertions {

  val globalAssertions = Seq(
    global.responseTime.max.lt(1000)
  )

  val slowAssertion = Seq(
    global.responseTime.max.lt(5000),
    global.responseTime.percentile4.lt(3000)
  )

  def scaledAssertions(factor: Double = 1.0) = Seq(
    global.responseTime.max.lt((1000 * factor).toInt),
    global.responseTime.percentile3.lt((500 * factor).toInt),
    global.responseTime.mean.lt((400 * factor).toInt)
  )
}
