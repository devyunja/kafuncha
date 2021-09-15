package services

import models.{DailyChatCount, KafunchaModel, SparkSessionProvider}
import org.apache.spark.sql.DataFrame
import org.apache.spark.sql.functions.col
import org.apache.spark.sql.types.DataTypes

import javax.inject.Singleton

@Singleton
class DailyChatCountService extends SparkSessionProvider with KafunchaService {
  override def toModels(sourcePath: String, kafunchaServiceOption: Option[KafunchaServiceOption]): Seq[KafunchaModel] = {
    toDf(sourcePath)
      .collect().toSeq.map { row =>
      val date = row.getAs[String]("DateOnly")
      val count = row.getAs[Long]("count(Message)")
      DailyChatCount(date, count)
    }
  }

  def toDf(sourcePath: String): DataFrame = readCsv(sourcePath)
    .withColumn("DateOnly", col("Date").cast(DataTypes.DateType))
    .withColumn("DateOnly", col("DateOnly").cast(DataTypes.StringType))
    .groupBy(col("DateOnly"))
    .agg(Map("Message" -> "count"))
    .orderBy(col("DateOnly").desc_nulls_last)
}
