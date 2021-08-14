package services

import models.{ChatLine, SparkSessionProvider}
import org.apache.spark.sql.DataFrame

class ChatHistoryService extends SparkSessionProvider {
  def toModels: Seq[ChatLine] = chatDataFrame
    .collect().toSeq.map { row =>
      val date = row.getAs[String]("Date")
      val user = row.getAs[String]("User")
      val message = row.getAs[String]("Message")
      ChatLine(date, user, message)
    }
}
