package models

import play.api.libs.json.{Json, OFormat}

case object MentionedUser {
  implicit val format: OFormat[MentionedUser] = Json.format[MentionedUser]
}

case class MentionedUser(date: String, user: String, mentionCount: Long) extends KafunchaModel
