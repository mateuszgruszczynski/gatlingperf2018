//package com.performance.project.simulations
//
//import java.util.UUID
//
//import io.gatling.core.Predef._
//import io.gatling.core.action.builder.ActionBuilder
//import io.gatling.http.Predef._
//import scala.concurrent.duration._
//import scala.util.Random
//
//class DummySim extends Simulation{
//
//  def runAction : ActionBuilder = {
//    //TODO how to get parameter1 and parameter2 from session or feeder here.
//    http("test").get("http://google.com/${parameter1}")
//  }
//
//  def getRandomData() = Map("parameter1" -> UUID.randomUUID.toString, "parameter2" -> (Random.nextInt(100000) + 700000))
//
//  val feeder = Iterator.continually(getRandomData())
//
//  val grpcScenario = scenario("TestGRPC server")
//
//
//  def addToUserWalletByIdName(userId: String, amount: => Double) =
//  exec(session => session.set(amountName, Random.nextInt(1000) + 100))
//    .exec(session => {
//      println(">>>>")
//      println(session(amountName).as[Double])
//      println(amountName + " -> " + "${" + amountName + "}")
//      println(">>>>")
//      session
//    })
//
////    .feed(feeder)
////    .exec(session => {
////      val parameter1= session("parameter1").as[String] //here is works
////      val parameter2= session("parameter2").as[Int] //here is works
////      println("parameter1: " + parameter1+ "parameter2: " + parameter2) //print diffrent for each execution
////      session
////    })
////    .exec(runAction)
//
//  setUp(
//    grpcScenario.inject(
//      constantUsersPerSec(2) during (5 seconds))
//  )
//
//
//   System.currentTimeMillis()
//}
//
