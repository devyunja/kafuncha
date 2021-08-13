package models

import play.api.libs.json._

case object DailyChampion {
  implicit val format: OFormat[DailyChampion] = Json.format[DailyChampion]
}

case class DailyChampion(date: String, user: String, messageCount: Long)
