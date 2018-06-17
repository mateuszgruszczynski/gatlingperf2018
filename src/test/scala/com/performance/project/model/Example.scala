package com.performance.project.model

import io.gatling.core.Predef._

object Example {

  val dataSource = Array(
    Map("data" -> """{"first": 1, "second": 111, "third": "test1"}"""),
    Map("data" -> """{"first": 2, "second": 222, "third": "test2"}"""),
    Map("data" -> """{"first": 3, "second": 333, "third": "test3"}"""),
    Map("data" -> """{"first": 4, "second": 444, "third": "test4"}"""),
    Map("data" -> """{"first": 5, "second": 555, "third": "test5"}""")
  )

  val exampleFeeder = dataSource.circular

}
