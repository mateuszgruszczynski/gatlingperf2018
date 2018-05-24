package com.performance.project.steps

import com.performance.project.model.ScreeningDataProvider
import io.gatling.core.body.StringBody
import io.gatling.core.Predef._
import io.gatling.http.Predef._

object ScreeningsSteps extends StepsBase {
  def addMovieScreening(idKey: String) = // jako parametr przyjmuje klucz pod którym ma szukac id filmu
    http("Add screening")
      .post("/screenings")
      .body(StringBody(session => jsonStringFromObject(ScreeningDataProvider.screeningMovie(session(idKey).as[Int])))) // generuje nowy screening z uzyciem id filmu wyciągnietego z sesji
      .asJSON
}