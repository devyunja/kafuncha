package services

import org.scalatestplus.play.PlaySpec
import org.scalatestplus.play.guice.GuiceOneAppPerTest
import play.api.test.Injecting

class AttendanceServiceSpec extends PlaySpec with GuiceOneAppPerTest with Injecting {
  "AttendanceService#toModels" should {
    "get correct list of model" in {
      val attendanceService = inject[AttendanceService]
      attendanceService.toModels.foreach(println)
    }
  }
}
