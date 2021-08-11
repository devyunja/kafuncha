package services

import models.{ChatLine, SparkSessionProvider}
import org.apache.spark.sql.DataFrame

class ChatHistoryService extends SparkSessionProvider {
  def readChatCsv(filePath: String): DataFrame = spark.read
    .option("multiline", "true")
    .option("inferSchema", "true")
    .option("dateFormat", "MMM dd YYYY")
    .option("header", "true")
    .option("sep", ",")
    .option("nullValue", "")
    .option("quote", "\"")
    .option("escape", "\"")
    .csv(filePath)

  def toModels(chatDataFrame: DataFrame): Seq[ChatLine] = {
    chatDataFrame.collect().map { row =>
      val date = row.getAs[String]("Date")
      val user = row.getAs[String]("User")
      val message = row.getAs[String]("Message")
      ChatLine(date, user, message)
    }.toSeq
  }
}
