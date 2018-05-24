package com.performance.project.steps

import com.performance.project.model.{Movie, Screening, ScreeningDataProvider}
import com.performance.project.steps.MoviesSteps.jsonStringFromObject
import io.gatling.http.Predef.{http, status}
import io.gatling.core.Predef._
import io.gatling.http.Predef._

import scala.util.Random

object MoviesSteps extends StepsBase {

  val getMovies = http("Get movie").get("/movies").check(status.is(200))

  def getMovie(id: => Int) = http("Get movie")
    .get(s"/movies/${id}")
    .check(status.is(200))
    .check(bodyString.transform(s => readJsonBodyAs[Movie](s).id == id).is(true))

  def getMoviesPage(page: => Int) = http("Get movie page").get(s"/movies?page=${page}").check(status.is(200))

  def addMovie(movie: => Movie) = http("Add movie")
    .post("/movies")
    .body(StringBody(session => jsonStringFromObject(movie)))
    .check(jsonPath("$.id").ofType[Int].saveAs("movieId"))
    .asJSON

  def addMovieAndSaveId(movie: => Movie, idKey: String) = http("Add movie")
    .post("/movies")
    .body(StringBody(session => jsonStringFromObject(movie)))
    .check(jsonPath("$.id").ofType[Int].saveAs(idKey)) // Wyciągamy z json id jako Int i zapisujemy pod kluczem "movieId"
    .asJSON

  def searchMovieByTitlePart(titlePart: String) = http("Search movie")
    .get(s"/movies/search?title=${titlePart}").check(status.is(200))

  def getMovieAndSaveTitleAsAttribute(id: => Int, titleKey: String) = http("Get movie")
    .get(s"/movies/${id}").check(jsonPath("$.title").saveAs(titleKey))

  def searchMovieByTitleFromAttribute(titleKey: String) = http("Search movie")
    .get("/movies/search?title=${" + titleKey + "}").check(status.is(200))
}