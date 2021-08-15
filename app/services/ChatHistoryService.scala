package services

import models.{ChatLine, SparkSessionProvider}

class ChatHistoryService extends SparkSessionProvider {
  def toModels: Seq[ChatLine] = chatDataFrame
    .collect().toSeq.map { row =>
      val date = row.getAs[String]("Date")
      val user = row.getAs[String]("User")
      val message = row.getAs[String]("Message")
      ChatLine(date, user, message)
    }

  def toModels(page: Int, offset: Int): Seq[ChatLine] =
    ((offset * page) - offset until offset * page).map(toModels.reverse(_))

}
