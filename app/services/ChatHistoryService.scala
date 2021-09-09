package services

import models.{ChatLine, KafunchaModel, SparkSessionProvider}

import javax.inject.Singleton

@Singleton
class ChatHistoryService extends SparkSessionProvider with KafunchaService {
  override def toModels(sourcePath: String): Seq[KafunchaModel] = {
    readCsv(sourcePath).collect().map { row =>
      val date = row.getAs[String]("Date")
      val user = row.getAs[String]("User")
      val message = row.getAs[String]("Message")
      ChatLine(date, user, message)
    }
  }

  def toPagedModels(sourcePath: String, page: Int, offset: Int): Seq[KafunchaModel] =
    ((offset * page) - offset until offset * page).map(toModels(sourcePath).reverse(_))
}
