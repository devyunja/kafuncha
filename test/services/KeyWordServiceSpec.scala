package services

import org.scalatestplus.play.PlaySpec
import org.scalatestplus.play.guice.GuiceOneAppPerTest
import play.api.test.Injecting

class KeyWordServiceSpec extends PlaySpec with GuiceOneAppPerTest with Injecting {
  "KeyWordService" should {
    "work correctly" in {
      val keyWordService =  inject[KeyWordService]
      val dailyKeyWordCount = keyWordService.dailyKeyWordCount
      dailyKeyWordCount.foreach(println)
    }
  }
}
