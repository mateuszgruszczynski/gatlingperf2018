package com.performance.project.config

import io.gatling.core.Predef._
import io.gatling.core.protocol.Protocol
import io.gatling.http.Predef._

object CinemaProtocols {
  val loadBalancer = http.baseURL("http://10.10.0.100:9000")

  val gateway = http.baseURL("http://api-gateway.softwareqa.pl")

  val api1 = http.baseURL("http://api1.softwareqa.pl")

  val api2 = http.baseURL("http://api2.softwareqa.pl")

  val api3 = http.baseURL("http://api3.softwareqa.pl")
}
