package services

import models.{DailyChatCount, SparkSessionProvider}
import org.apache.spark.sql.DataFrame
import org.apache.spark.sql.functions.col
import org.apache.spark.sql.types.DataTypes

import javax.inject.Singleton

@Singleton
class DailyChatCountService extends SparkSessionProvider {
  def toModels: Seq[DailyChatCount] = toDf
    .collect().toSeq.map { row =>
      val date = row.getAs[String]("DateOnly")
      val count = row.getAs[Long]("count(Message)")
      DailyChatCount(date, count)
    }

  def toDf: DataFrame = chatDataFrame
    .withColumn("DateOnly", col("Date").cast(DataTypes.DateType))
    .withColumn("DateOnly", col("DateOnly").cast(DataTypes.StringType))
    .groupBy(col("DateOnly"))
    .agg(Map("Message" -> "count"))
    .orderBy(col("DateOnly").desc_nulls_last)
}
