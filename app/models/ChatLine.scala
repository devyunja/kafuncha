package models

import play.api.libs.json._

case object ChatLine {
  implicit val format: OFormat[ChatLine] = Json.format[ChatLine]
}

case class ChatLine(date: String, user: String, message: String)
