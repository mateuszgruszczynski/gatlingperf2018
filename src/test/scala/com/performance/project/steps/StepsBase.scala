package com.performance.project.steps

import io.gatling.core.Predef._
import scala.reflect.classTag

abstract class StepsBase {

  import org.json4s.DefaultFormats
  import org.json4s.jackson.Serialization.write
  import org.json4s.jackson.Serialization.read

  implicit val formats = DefaultFormats

  // Generuje JSON w postaci stringa na bazie przekazanego obiektu
  def jsonStringFromObject(obj: => AnyRef) = write(obj)

  // def jsonBodyFromObject(obj: => AnyRef) = StringBody(s => write(obj)) <-- Tego się pozbywam i zastępuje to tą metodą z góry

  def readJsonBodyAs[T](jsonString: => String)(implicit m: Manifest[T]) = try {
    read[T](jsonString)
  } catch {
    case e: Exception => throw new Exception(s"Cannot deserialize ${classTag[T]} from \n${jsonString}\n" + e.getMessage)
  }
}
