package services

import org.joda.time.format.DateTimeFormat
import org.scalatestplus.play.PlaySpec
import org.scalatestplus.play.guice.GuiceOneAppPerTest
import play.api.test.Injecting

class KeywordServiceSpec extends PlaySpec with GuiceOneAppPerTest with Injecting {
  "KeyWordService" should {
    "work correctly" in {
      val keywordService =  inject[KeywordService]
      val todayKeyWordCount = keywordService.todayKeywordCount
      todayKeyWordCount.foreach(println)
    }
  }

  "KeyWordService#parseDate(String): String" should {
    "work correctly" in {
      val keywordService = inject[KeywordService]
      val dateTimeFormat = DateTimeFormat.forPattern("yyyy-MM-dd")
      val inputExamples = Seq("2021-08-20", "08-20-2021", "20-08-2021", "2012-08-200", "2021/08/20")
      inputExamples.foreach { dateString =>
        println(keywordService.parseDate(dateString, dateTimeFormat))
        println(keywordService.parseDate(dateString))
      }
    }
  }

  "KeyWordService#messagesDfByDate(String): DataFrame" should {
    "work correctly" in {
      val keywordService = inject[KeywordService]
      val messagesDfByDate = keywordService.messagesDfByDate("2021-08-19")

      messagesDfByDate.show(200)
      messagesDfByDate.printSchema()
    }
  }

  "KeyWordService#keywordCountByDate(String): Seq[KeywordCount]" should {
    "work correctly" in {
      val keywordService = inject[KeywordService]
      val keywordCounts = keywordService.keywordCountByDate("2021-08-20")

      keywordCounts.foreach(println)
    }
  }

  "KeyWordService#bulkKeywordCountByDates(dates: Seq[String]): Seq[Seq[KeywordCount]]" should {
    "work correctly" in {
      val keywordService = inject[KeywordService]
      val inputExamples = Seq("2021-08-20", "2021-08-19", "2021-08-01")
      val bulkKeywordCountByDates = keywordService.bulkKeywordCountByDates(inputExamples)

      bulkKeywordCountByDates.foreach(println)
    }
  }
}
