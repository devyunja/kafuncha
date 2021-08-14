package services

import org.scalatestplus.play.PlaySpec
import org.scalatestplus.play.guice.GuiceOneAppPerTest
import play.api.test.Injecting

class DailyChatCountServiceSpec extends PlaySpec with GuiceOneAppPerTest with Injecting {
  "DailyChatCountServiceSpec#toModels" should {
    "get correct list of models" in {
      val dailyChatCountService = inject[DailyChatCountService]
      val dailyChatCountServiceDf = dailyChatCountService.toDf
      val models = dailyChatCountService.toModels
      dailyChatCountServiceDf.show(100)
      dailyChatCountServiceDf.printSchema()
      models.foreach(println)
    }
  }
}
