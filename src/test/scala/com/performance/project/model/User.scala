package com.performance.project.model

import scala.util.Random

case class User(
  id: Option[Int] = None,
  firstname: String,
  lastname: String,
  email: String,
  username: String,
  city: String,
  address: String,
  birthdate: String,
  password: String,
)

object UserDataProvider{

  def randomUser = User(
    None,
    Random.alphanumeric.take(20).mkString,
    Random.alphanumeric.take(20).mkString,
    Random.alphanumeric.take(20).mkString + "@gmail.com",
    Random.alphanumeric.take(20).mkString,
    Random.alphanumeric.take(20).mkString,
    Random.alphanumeric.take(20).mkString,
    "2000-01-01",
    Random.alphanumeric.take(20).mkString
  )

}