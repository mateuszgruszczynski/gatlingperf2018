package com.performance.project.config

import io.gatling.core.Predef._
import io.gatling.core.protocol.Protocol
import io.gatling.http.Predef._

object CinemaProtocols {

  val loadBalancer = http.baseURL(Config.baseUrl + ":9000")

  val gateway = http.baseURL(Config.baseUrl + ":8000")

  val api1 = http.baseURL(Config.baseUrl + ":9010")

  val api2 = http.baseURL(Config.baseUrl + ":9020")

  val api3 = http.baseURL(Config.baseUrl + ":9030")
}
