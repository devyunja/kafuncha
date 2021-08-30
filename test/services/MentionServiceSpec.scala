package services

import org.scalatestplus.play.PlaySpec
import org.scalatestplus.play.guice.GuiceOneAppPerTest
import play.api.test.Injecting

class MentionServiceSpec extends PlaySpec with GuiceOneAppPerTest with Injecting {
  "MentionService" should {
    "work correctly" in {
      val mentionService = inject[MentionService]
    }
  }
}
