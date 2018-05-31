package com.performance.project.config

import java.io.File

import com.typesafe.config.ConfigFactory


object Config extends App{

  val configuration = ConfigFactory.parseFile(new File("src/test/resources/application.conf"))

  val loadBalancerUrl = configuration.getString("loadBalanceUrl")

  val maxUsers = configuration.getInt("maxUsers")

  val gatewayHost = configuration.getString("gateway.host")

  val gatewayConfig = configuration.getConfig("gateway")

  val gatewayHost2 = gatewayConfig.getString("host")

  val env = configuration.getString("env")

  val host = configuration.getConfig(env).getString("host")
  val port = configuration.getConfig(env).getInt("port")

}