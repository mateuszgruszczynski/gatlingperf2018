package com.performance.project.steps

import com.performance.project.model.{AuthorizationRequest, Movie, User, UserDataProvider}
import io.gatling.core.Predef._
import io.gatling.core.session.Expression
import io.gatling.http.Predef._
import io.gatling.http.check.HttpCheck
import io.gatling.http.check.HttpCheckScope.Url

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

  def addUser(user: => User, extraChecks: HttpCheck*) = {
    http("Add user").post("/users")
      .body(StringBody(session => jsonStringFromObject(user)))
      .check(extraChecks: _*) // a, b, c, d
      //.check(extraChecks) // Seq[a, b, c, d]
      .check(jsonPath("$[*].genre"))
  }

  exec(addUser(UserDataProvider.randomUser, status.is(200), jsonPath("$.id").exists.saveAs("userId")))
  exec(addUser(UserDataProvider.randomUser))

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

  def getMoviesByGenre(movieGenre: String) = http("Get movie title by genie")
    .get("/movies/search?genre=${" + movieGenre + "}")
    .check(status.is(200))
    .check(jsonPath("$[0].genre").is("${" + movieGenre + "}"))
    .check(bodyString.transform(
      (movies, session) => readJsonBodyAs[List[Movie]](movies)
        .forall(
          m => m.genre == session(movieGenre).as[String]
        )
    ).is(true))

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





  def getUserWalletValue(userId: String, userToken: String) = http("get user wallet value")
    .get(s"/users/$${$userId}/wallet")  // /users/${userIdAtribute}/wallet
    .header("Http-Auth-Token", "${userToken}")
    .check(jsonPath("$.value").exists.saveAs("userWalletValue"))

  def getUserWalletValueById(userId: => Int, userToken: String) = http("get user wallet value")
    .get(s"/users/$${$userId}/wallet")  // /users/${userIdAtribute}/wallet
    .header("Http-Auth-Token", "${userToken}")
    .check(jsonPath("$.value").exists.saveAs("userWalletValue"))

  def getUserAndCheckId(userIdAttr: String, userTokenAttr: String) = http("get user")
    .get(s"/users/$${$userIdAttr}")
    .check(jsonPath("$.id").is(s"$${$userIdAttr}"))

  exec(getUserWalletValue("userIdAtribute", "userTokenAttribute"))

  //  exec(getUserWalletValueById(Random.nextInt(10)))


  def authenticateUserAndSaveToken(userAttrName: String, tokenAttrName: String, idAttrName: String) = {
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
      .check(jsonPath("$.token").exists.saveAs(tokenAttrName))
      .check(jsonPath("$.user.id").exists.saveAs(idAttrName))
    )
  }

  def authenticateUserAndSaveTokenWithExpression(se: Expression[String], userAttrName: String, tokenAttrName: String, idAttrName: String) = {
    exec(http("Authenticate user").post("/authenticate")
      .body(
        StringBody(
          se
        )
      ).asJSON
      .check(jsonPath("$.token").exists.saveAs(tokenAttrName))
      .check(jsonPath("$.user.id").exists.saveAs(idAttrName))
    )
  }

  //  exec(authenticateUserAndSaveTokenWithExpression(
  //    session => jsonStringFromObject(
  //    AuthorizationRequest(
  //      session("").as[User].username,
  //      session("").as[User].password
  //    )
  //  ), "aaaa", "bbbb", "ccccc"))
  //

  def authenticateUserAndSaveToFile(userAttrName: String, tokenAttrName: String) = {
    exec(http("Authenticate user").post("/authenticate")
      .body(StringBody(_ => s"""... ${System.currentTimeMillis()} ...""" ))
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

  def getUserWalletValue(idAttrName: String, tokenAttrName: String, valueAttrName: String, extraChecks: HttpCheck*) =
    http("Get user wallet")
      .get("/users/${" + idAttrName + "}/wallet")
      .header("Http-Auth-Token", "${"+ tokenAttrName +"}")
      .check(status.is(200))
      .check(jsonPath("$.value").ofType[Double].exists.saveAs(valueAttrName))
      .check(extraChecks: _*)

//  exec(getUserWalletValue("userData", "userToken", "walletAmount")
//
//  exec(getUserWalletValue("userData", "userToken", "walletAmount", jsonPath("$.id").exists, responseTimeInMillis.lessThan(500)))
//
//  exec(getUserWalletValue("userData", "userToken", "walletAmount", responseTimeInMillis.lessThan(1000)))



  def payIntoUserWallet(idAttrName: String, tokenAttrName: String, value: Double) =
    http("Pay into wallet")
      .get("/users/${" + idAttrName + s"}/wallet/payin?value=$value")
      .header("Http-Auth-Token", "${"+ tokenAttrName +"}")
      .check(status.is(200))
      .check(bodyString.is("OK"))
}
