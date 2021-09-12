package services

import org.scalatestplus.play.PlaySpec
import org.scalatestplus.play.guice.GuiceOneAppPerTest
import play.api.test.Injecting

class DatabaseServiceSpec extends PlaySpec with GuiceOneAppPerTest with Injecting {
  "DatabaseService" should {
    "work correctly" in {
      val service = inject[DatabaseService]
      service.connectionTest()

      Thread.sleep(1000 * 60 * 5)
    }
  }
}
