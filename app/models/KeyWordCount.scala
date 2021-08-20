package models

import play.api.libs.json.{Json, OFormat}

case object KeyWordCount {
  implicit val format: OFormat[KeyWordCount] = Json.format[KeyWordCount]
}

case class KeyWordCount(keyWord: String, count: Long)
