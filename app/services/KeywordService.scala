package services

import models.{KeywordCount, SparkSessionProvider}
import org.apache.spark.sql.DataFrame
import org.apache.spark.sql.functions.{col, current_date}
import org.apache.spark.sql.types.DataTypes
import org.joda.time.DateTime
import org.joda.time.format.{DateTimeFormat, DateTimeFormatter}

import javax.inject.Singleton

object RangeChoices extends Enumeration {
  val DAILY, WEEKLY = Value
}

@Singleton
class KeywordService extends SparkSessionProvider {
  def todayMessagesDf: DataFrame = chatDataFrame
    .withColumn("DateOnly", col("Date").cast(DataTypes.DateType))
    .filter(col("DateOnly") === current_date())
    .orderBy(col("Date").desc_nulls_last)

  def extractMessages(chatDataFrame: DataFrame): Seq[String] = chatDataFrame.collect().map { row =>
    row.getAs[String]("Message")
  }

  def extractWords(chatDataFrame: DataFrame): Seq[String] = extractMessages(chatDataFrame)
    .foldLeft(Seq[String]()) { (result, current) =>
      result ++ current.split(" ")
    }

  def groupByWord(chatDataFrame: DataFrame): DataFrame = {
    import spark.implicits._
    extractWords(chatDataFrame).toDF("Word")
      .groupBy("Word").count().orderBy(col("count").desc_nulls_last)
  }

  def todayKeywordCount: Seq[KeywordCount] = groupByWord(todayMessagesDf).collect().map { row =>
    KeywordCount("today", row.getAs[String]("Word"), row.getAs[Long]("count"))
  }

  def parseDate(stringDate: String, dateTimeFormatter: DateTimeFormatter)
      : Either[IllegalArgumentException, DateTime] = try {
    Right(dateTimeFormatter.parseDateTime(stringDate))
  } catch {
    case exception: IllegalArgumentException => Left(exception)
  }

  def parseDate(date: String): String = {
    val dateFormat = DateTimeFormat.forPattern("yyyy-MM-dd")
    if (parseDate(date, dateFormat).isRight) date
    else ""
  }

  def messagesDfByDate(date: String): DataFrame = chatDataFrame
    .withColumn("DateOnly", col("Date").cast(DataTypes.DateType))
    .filter(col("DateOnly") === parseDate(date))

  def keywordCountByDate(date: String): Seq[KeywordCount] = groupByWord(messagesDfByDate(date)).collect().map { row =>
    KeywordCount(date, row.getAs[String]("Word"), row.getAs[Long]("count"))
  }

  def bulkKeywordCountByDates(dates: Seq[String]): Seq[Seq[KeywordCount]] = {
    dates.map { date =>
      keywordCountByDate(date)
    }
  }
}
