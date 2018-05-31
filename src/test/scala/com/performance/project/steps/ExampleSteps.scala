package com.performance.project.steps

import com.performance.project.model.{AuthorizationRequest, User}
import io.gatling.core.Predef._
import io.gatling.http.Predef._

import scala.reflect.io.File
import scala.util.Random

object ExampleSteps extends StepsBase {

  // Pobieranie zawsze pierwszego
  def getFirstMovie = {
    http("Get movie 1").get("/movies/1")
  }

  // Pobieranie losowego filmu ale test samego dla każdego użytkownika
  def getRandomMovie = {
    http("Get random movie").get(s"/movies/${Random.nextInt(4)+1}")
  }

  // Pobieranie losowego filmu, ale zawsze tą sama metodą
  def getRandomMovieWithSE = {
    http("Get random movie").get(session => s"/movies/${Random.nextInt(4)+1}")
  }

  // Pobieranie filmu o ID - call by value
  def getMovieById(id: Int) = {
    http("Get movie by id").get(_ => s"/movies/${id}")
  }

  // Pobieranie filmu o ID - call by name
  def getMovieByIdVariable(id: => Int) = {
    http("Get movie by id").get(_ => s"/movies/${id}")
  }

  //--------------------------------------------------

  //Zapisanie jednej wartości do stałego atrybutu
  def saveMovieTitle = {
    http("Get first movie and save title").get("/movies/1")
      .check(jsonPath("$.title").saveAs("movieTitle"))
  }

  //Czytanie parametru ze stałego atrybutu
  def searchMovieTitle = {
    http("Search movie").get("/movies/search?title=${movieTitle}")
  }

  //---------------------------------------------------

  //Zapisywanie jednej wartości do konkretnego atrybutu
  def saveMovieTitleAsAtribute(attributeName: String) = {
    http("Get first movie and save title").get("/movies/1")
      .check(jsonPath("$.title").saveAs(attributeName))
  }

  // Czytanie wartości z atrybutu
  def searchMovieByTitleAttr(attributeName: String) = {
    http("Search movie").get("/movies/search?title=${" + attributeName + "}")
  }

  //------------------------------------------------------------

  // Dodawanie użytkownika z użyciem call by name

  def addUser(user: => User) = {
    http("Add user").post("/users")
      .body(StringBody(session => jsonStringFromObject(user)))
  }

  //Zapisywanie hasła do sesji jest problematyczne w takim wypadku:
  def addUserAndSavePassword(user: => User) = {
    exec(session => session.set("password", user.password))
      .exec(http("Add user").post("/users").body(StringBody(session => jsonStringFromObject(user))).asJSON)
  }
  // hasło zapisane do sesji i w użytkowniku będzie inne bo mamy call by name

  // Można zapisać cały obiekt do sesji
  def addUserAndSaveRequestData(user: => User, userAttrName: String) = {
    exec(session => session.set(userAttrName, user))
      .exec(http("Add user").post("/users")
        .body(StringBody(session => jsonStringFromObject(session(userAttrName).as[User]))).asJSON
      )
  }

  //Budowanie zapytania z 2 atrybutów
  def authenticateUser(userAttrName: String) = {
    http("Authenticate user").post("/authenticate")
      .body(
        StringBody(
          session => jsonStringFromObject(
            AuthorizationRequest(
              session(userAttrName).as[User].username,
              session(userAttrName).as[User].password
            )
          )
        )
      )
  }

  def authenticateUserAndSaveToken(userAttrName: String, tokenAttrName: String) = {
    exec(http("Authenticate user").post("/authenticate")
      .body(
        StringBody(
          session => jsonStringFromObject(
            AuthorizationRequest(
              session(userAttrName).as[User].username,
              session(userAttrName).as[User].password
            )
          )
        )
      ).asJSON
      .check(jsonPath("$.token").exists.saveAs(tokenAttrName)))
  }

    def authenticateUserAndSaveToFile(userAttrName: String, tokenAttrName: String) = {
      exec(http("Authenticate user").post("/authenticate")
        .body(
          StringBody(
            session => jsonStringFromObject(
              AuthorizationRequest(
                session(userAttrName).as[User].username,
                session(userAttrName).as[User].password
              )
            )
          )
        ).asJSON
        .check(jsonPath("$.token").exists.saveAs(tokenAttrName)))
        .exec(session => {
          File("sampleFile.txt")
            .createFile()
            .appendAll(s"${session(userAttrName).as[User].username}, ${session(userAttrName).as[User].password}, ${session(tokenAttrName).as[String]}\n")
          session
        })
    }


}
