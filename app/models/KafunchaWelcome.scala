package models

import play.api.libs.json._

case object KafunchaWelcome {
  implicit val format: OFormat[KafunchaWelcome] = Json.format[KafunchaWelcome]
}

case class KafunchaWelcome(message: String)
