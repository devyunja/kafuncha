package services

import org.scalatestplus.play.PlaySpec
import org.scalatestplus.play.guice.GuiceOneAppPerTest
import play.api.test.Injecting

class ChatHistoryServiceSpec extends PlaySpec with GuiceOneAppPerTest with Injecting {
  "ChatHistoryService#readChatCsv" should {
    "read csv correctly" in {
      val chatHistoryService = inject[ChatHistoryService]
      val dataFrame =
        chatHistoryService.readChatCsv("conf/chat_history.csv")
      dataFrame.show(100)
      dataFrame.printSchema()
    }
  }

  "ChatHistoryService#toModels" should {
    "get correct list of model" in {
      val chatHistoryService = inject[ChatHistoryService]
      val dataFrame =
        chatHistoryService.readChatCsv("conf/chat_history.csv")
      val models = chatHistoryService.toModels(dataFrame)
      models.take(200).foreach(println)
      println(models.reverse.head)
    }
  }
}
