package services

import org.scalatestplus.play.PlaySpec
import org.scalatestplus.play.guice.GuiceOneAppPerTest
import play.api.test.Injecting

class ChatHistoryServiceSpec extends PlaySpec with GuiceOneAppPerTest with Injecting {
  "ChatHistoryService#toModels" should {
    "get correct list of models" in {
      val chatHistoryService = inject[ChatHistoryService]
    }
  }
}
