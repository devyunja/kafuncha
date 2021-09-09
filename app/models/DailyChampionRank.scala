package models

import play.api.libs.json.{Json, OFormat}

case object DailyChampionRank {
  implicit val format: OFormat[DailyChampionRank] = Json.format[DailyChampionRank]
}

case class DailyChampionRank(date: String, user: String, messageCount: Long, rank: Int) extends KafunchaModel
