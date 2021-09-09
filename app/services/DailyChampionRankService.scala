package services

import models.{DailyChampionRank, KafunchaModel, SparkSessionProvider}
import org.apache.spark.sql.DataFrame
import org.apache.spark.sql.expressions.Window
import org.apache.spark.sql.functions.{col, current_date, date_sub, lit, max, rank}
import org.apache.spark.sql.types.DataTypes

import javax.inject.Singleton

@Singleton
class DailyChampionRankService extends SparkSessionProvider with KafunchaService {
  override def toModels(sourcePath: String): Seq[KafunchaModel] = toDf(sourcePath)
    .collect().map { row =>
      val data = row.getAs[String]("DateOnly")
      val user = row.getAs[String]("User")
      val messageCount = row.getAs[Long]("count(Message)")
      val rank = row.getAs[Int]("rank")
      DailyChampionRank(data, user, messageCount, rank)
    }

  def toDf(sourcePath: String): DataFrame = {
    val df = readCsv(sourcePath)
      .withColumn("DateOnly", col("Date").cast(DataTypes.DateType))
      .withColumn("DateOnly", col("DateOnly").cast(DataTypes.StringType))
      .groupBy(col("DateOnly"), col("User"))
      .agg(Map("Message" -> "count"))
      .orderBy(col("DateOnly").desc_nulls_last)

    val rankWindow = Window
      .partitionBy("DateOnly")
      .orderBy(col("count(Message)").desc_nulls_last)

    df.withColumn("rank", rank().over(rankWindow))
  }

  def toModels(sourcePath: String, rewindNumDays: Int): Seq[DailyChampionRank] = {
    val df = toDf(sourcePath)
    val firstDate = df.collect().head.getAs[String]("DateOnly")
    df.withColumn("Date", col("DateOnly").cast(DataTypes.DateType))
      .filter(col("Date").geq(date_sub(lit(firstDate), rewindNumDays)))
      .collect().map { row =>
      val data = row.getAs[String]("DateOnly")
      val user = row.getAs[String]("User")
      val messageCount = row.getAs[Long]("count(Message)")
      val rank = row.getAs[Int]("rank")
      DailyChampionRank(data, user, messageCount, rank)
    }
  }
}
