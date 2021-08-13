package services

import models.{DailyChatCount, SparkSessionProvider}
import org.apache.spark.sql.DataFrame
import org.apache.spark.sql.functions.col
import org.apache.spark.sql.types.DataTypes

class DailyChatCountService extends SparkSessionProvider {
  def toModels(chatDataFrame: DataFrame): Seq[DailyChatCount] = toDf(chatDataFrame)
    .collect().toSeq.map { row =>
      val date = row.getAs[String]("DateOnly")
      val count = row.getAs[Long]("count(Message)")
      DailyChatCount(date, count)
    }

  def toDf(chatDataFrame: DataFrame): DataFrame = chatDataFrame
    .withColumn("DateOnly", col("Date").cast(DataTypes.DateType))
    .withColumn("DateOnly", col("DateOnly").cast(DataTypes.StringType))
    .groupBy(col("DateOnly"))
    .agg(Map("Message" -> "count"))
    .orderBy(col("DateOnly").desc_nulls_last)
}
