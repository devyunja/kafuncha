package services

import org.scalatestplus.play.PlaySpec
import org.scalatestplus.play.guice.GuiceOneAppPerTest
import play.api.test.Injecting

class LinksInChatServiceSpec extends PlaySpec with GuiceOneAppPerTest with Injecting {
  "LinksInChat#toModels" should {
    "get correct list of models" in {
      val linksInChatService =  inject[LinksInChatService]
      val chatDf = linksInChatService.readChatCsv("conf/chat_history.csv")
      val df = linksInChatService.toDf(chatDf)
      df.show(100)
      df.printSchema()
      val models = linksInChatService.toModels(chatDf)
      models.foreach(println)
    }
  }
}
