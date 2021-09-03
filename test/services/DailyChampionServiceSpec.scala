package services

import org.scalatestplus.play.PlaySpec
import org.scalatestplus.play.guice.GuiceOneAppPerTest
import play.api.test.Injecting

class DailyChampionServiceSpec extends PlaySpec with GuiceOneAppPerTest with Injecting {
  "DailyChampionService#toModels" should {
    "get correct list of models" in {
      val dailyChampionService = inject[DailyChampionService]
      val df = dailyChampionService.toDf("conf/chat_history.csv")
      df.show(100)
      df.printSchema()
      val models = dailyChampionService.toModels("conf/chat_history.csv")
      models.foreach(println)
    }
  }
}
