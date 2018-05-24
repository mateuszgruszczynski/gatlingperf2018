package com.performance.project.steps

import com.performance.project.model.AuthorizationRequest
import io.gatling.core.Predef._
import io.gatling.core.Predef.StringBody
import io.gatling.http.Predef.{http, jsonPath, status}

object UserSteps extends StepsBase {

  def authenticateUser(request: AuthorizationRequest) = http("Auth user")
    .post("/authenticate")
    .body(StringBody(jsonStringFromObject(request)))
    .asJSON
    .check(status.is(200))
    .check(jsonPath("$.token").exists.saveAs("userToken"))

  def authenticateUserAlt = http("Auth user")
    .post("/authenticate")
    .body(StringBody(jsonStringFromObject(AuthorizationRequest(("${username}"), ("${password}")))))
    .asJSON
    .check(status.is(200))
    .check(jsonPath("$.token").exists.saveAs("userToken"))

}
