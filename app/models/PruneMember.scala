package models

import play.api.libs.json._

case object PruneMember {
  implicit val format: OFormat[PruneMember] = Json.format[PruneMember]
}

case class PruneMember(user: String, lastShowDate: String, today: String, dataDiff: Int) extends KafunchaModel
