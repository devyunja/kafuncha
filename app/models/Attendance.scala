package models

import play.api.libs.json._

case object Attendance {
  implicit val format: OFormat[Attendance] = Json.format[Attendance]
}

case class Attendance(user: String, count: Long)
