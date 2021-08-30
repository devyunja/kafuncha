package services

import org.scalatestplus.play.PlaySpec
import org.scalatestplus.play.guice.GuiceOneAppPerTest
import play.api.test.Injecting

class ImposterServiceSpec extends PlaySpec with GuiceOneAppPerTest with Injecting {
  "ImposterService" should {
    "work correctly" in {
      val imposterService = inject[ImposterService]
    }
  }
}
