package services

import models.{KeyWordCount, SparkSessionProvider}
import org.apache.spark.sql.DataFrame
import org.apache.spark.sql.functions.{col, current_date}
import org.apache.spark.sql.types.DataTypes

object RangeChoices extends Enumeration {
  val DAILY, WEEKLY = Value
}

class KeyWordService extends SparkSessionProvider {
  def todayMessagesDf: DataFrame = chatDataFrame
    .withColumn("DateOnly", col("Date").cast(DataTypes.DateType))
    .filter(col("DateOnly") === current_date())
    .orderBy(col("Date").desc_nulls_last)

  def extractMessages: Seq[String] = todayMessagesDf.collect().map { row =>
    row.getAs[String]("Message")
  }

  def extractWords: Seq[String] = extractMessages.foldLeft(Seq[String]()) { (result, current) =>
    result ++ current.split(" ")
  }

  def groupByWord: DataFrame = {
    import spark.implicits._
    extractWords.toDF("Word").groupBy("Word").count().orderBy(col("count").desc_nulls_last)
  }

  def dailyKeyWordCount: Seq[KeyWordCount] = groupByWord.collect().map { row =>
    KeyWordCount(row.getAs[String]("Word"), row.getAs[Long]("count"))
  }
}
