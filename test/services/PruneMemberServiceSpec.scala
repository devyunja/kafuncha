package services

import org.scalatestplus.play.PlaySpec
import org.scalatestplus.play.guice.GuiceOneAppPerTest
import play.api.test.Injecting

class PruneMemberServiceSpec extends PlaySpec with GuiceOneAppPerTest with Injecting {
  "PruneMemberService#toModels" should {
    "get models correctly" in {
      val pruneMemberService = inject[PruneMemberService]
    }
  }
}
