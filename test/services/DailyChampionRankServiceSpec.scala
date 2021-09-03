package services

import org.scalatestplus.play.PlaySpec
import org.scalatestplus.play.guice.GuiceOneAppPerTest
import play.api.test.Injecting

class DailyChampionRankServiceSpec extends PlaySpec with GuiceOneAppPerTest with Injecting {
  "DailyChampionRankService" should {
    "work correctly" in {
      val dailyChampionRankService = inject[DailyChampionRankService]
      val df = dailyChampionRankService.toDf("conf/chat_history.csv")

      df.show(300)
      df.printSchema()

      val models = dailyChampionRankService.toModels("conf/chat_history.csv")
      models.foreach(println)
    }
  }
}
