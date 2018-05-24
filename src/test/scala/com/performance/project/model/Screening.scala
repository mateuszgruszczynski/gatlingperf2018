package com.performance.project.model

case class Screening(
  id: Option[Int] = None,
  movieid: Int,
  city: String,
  roomnumber: Int,
  seatslimit: Int,
  seatstaken: Int,
  time: String,
  price: Double
)

object ScreeningDataProvider{
  def screeningMovie(movieId: Int) : Screening = Screening(
    None,
    movieId,
    "Kraków",
    1,
    120,
    0,
    "2018-09-09:12:00",
    23.00
  )
}
