package models

import play.api.libs.json._

case object DailyChatCount {
  implicit val format: OFormat[DailyChatCount] = Json.format[DailyChatCount]
}

case class DailyChatCount(date: String, count: Long) extends KafunchaModel
