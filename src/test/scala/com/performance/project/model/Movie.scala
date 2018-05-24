package com.performance.project.model

import io.gatling.core.session.Expression


import io.gatling.core.session.{Expression, Session}

import scala.util.Random

case class Movie(
  id: Option[Int],
  title: String,
  director: String,
  genre: String,
  country: String,
  year: Int,
  tags: String,
  agelimit: Int
)

object MovieDataProvider{

  def randomMovie = Movie(
    None,
    title = Random.alphanumeric.take(10).mkString,
    director = Random.alphanumeric.take(10).mkString,
    genre = Random.alphanumeric.take(10).mkString,
    country = Random.alphanumeric.take(10).mkString,
    year = Random.nextInt(2000),
    tags = Random.alphanumeric.take(10).mkString,
    agelimit = Random.nextInt(50)
  )

  def movieWithTitle(t: String) = randomMovie.copy(title = t)

  def movieWithGenre(g: String) = randomMovie.copy(genre = g)

  def adultMovie = randomMovie.copy(agelimit = 18)

}