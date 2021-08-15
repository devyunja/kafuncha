package services

import org.scalatestplus.play.PlaySpec
import org.scalatestplus.play.guice.GuiceOneAppPerTest
import play.api.test.Injecting

class ChatHistoryServiceSpec extends PlaySpec with GuiceOneAppPerTest with Injecting {
  "ChatHistoryService#toModels" should {
    "get correct list of models" in {
      val chatHistoryService = inject[ChatHistoryService]
      val models = chatHistoryService.toModels
      models.take(200).foreach(println)
      println(models.reverse.head)
    }
  }

  "ChatHistoryService#toModels(page, offset)" should {
    "get correct list of models" in {
      val chatHistoryService = inject[ChatHistoryService]
      val modelsA = chatHistoryService.toModels(page = 1, offset = 10)
      val modelsB = chatHistoryService.toModels(page = 2, offset = 10)
      val modelsC = chatHistoryService.toModels(page = 3, offset = 10)
      modelsA.foreach(println)
      println("******************************************************")
      modelsB.foreach(println)
      println("******************************************************")
      modelsC.foreach(println)
    }
  }
}
