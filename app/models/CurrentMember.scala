package models

import play.api.libs.json._

case object CurrentMember {
  implicit val format: OFormat[CurrentMember] = Json.format[CurrentMember]
}

case class CurrentMember(list: Seq[String]) extends KafunchaModel
