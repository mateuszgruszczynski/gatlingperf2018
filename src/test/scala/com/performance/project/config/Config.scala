package com.performance.project.config

import com.typesafe.config.ConfigFactory

object Config{
  val config = ConfigFactory.load
  lazy val baseUrl = config.getString("baseUrl")
  lazy val baseLoad = config.getInt("baseLoad")
  lazy val baseDuration = config.getInt("baseDuration")
}