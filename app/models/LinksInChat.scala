package models

import play.api.libs.json._

case object LinksInChat {
  implicit val format: OFormat[LinksInChat] = Json.format[LinksInChat]
}

case class LinksInChat(date: String, user: String, url: String)
