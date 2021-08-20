package models

import play.api.libs.json.{Json, OFormat}

case object KeywordCount {
  implicit val format: OFormat[KeywordCount] = Json.format[KeywordCount]
}

case class KeywordCount(date: String, keyWord: String, count: Long)
