package com.performance.project.config

import io.gatling.core.Predef._
import io.gatling.http.Predef._

import io.gatling.core.Predef.jsonFile

object Feeders {

  val arrayFeeder = Array(
    Map("param1" -> 1, "param2" -> 2)
  )

  val usersFeeder = jsonFile("src/test/resources/users.json").circular

}
