package services

import models.{DailyChampion, KafunchaModel, SparkSessionProvider}
import org.apache.spark.sql.DataFrame
import org.apache.spark.sql.expressions.Window
import org.apache.spark.sql.functions.{col, max}
import org.apache.spark.sql.types.DataTypes

import javax.inject.Singleton

@Singleton
class DailyChampionService extends SparkSessionProvider with KafunchaService {
  override def toModels(sourcePath: String): Seq[KafunchaModel] = toDf(sourcePath: String).collect().toSeq.map { row =>
    val date = row.getAs[String]("DateOnly")
    val user = row.getAs[String]("User")
    val messageCount = row.getAs[Long]("max(count(Message))")
    DailyChampion(date, user, messageCount)
  }

  def toDf(sourcePath: String): DataFrame = {
    val df = readCsv(sourcePath)
      .withColumn("DateOnly", col("Date").cast(DataTypes.DateType))
      .withColumn("DateOnly", col("DateOnly").cast(DataTypes.StringType))
      .groupBy(col("DateOnly"), col("User"))
      .agg(Map("Message" -> "count"))
      .orderBy(col("DateOnly").desc_nulls_last)

    val window = Window.partitionBy(col("DateOnly"))

    df.withColumn("max(count(Message))", max("count(Message)").over(window))
      .filter(col("max(count(Message))") === col("count(Message)"))
      .drop(col("count(Message)"))
  }
}
