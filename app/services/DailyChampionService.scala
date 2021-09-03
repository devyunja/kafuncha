package services

import models.{DailyChampion, KafunchaModel, SparkSessionProvider}
import org.apache.spark.sql.DataFrame
import org.apache.spark.sql.expressions.Window
import org.apache.spark.sql.functions.{col, max}
import org.apache.spark.sql.types.DataTypes

import javax.inject.Singleton

@Singleton
class DailyChampionService extends SparkSessionProvider with KafunchaService {
  override def toModel(sourcePath: String): Seq[KafunchaModel] = {
    val window = Window.partitionBy(col("DateOnly"))
    readCsv(sourcePath)
      .withColumn("DateOnly", col("Date").cast(DataTypes.DateType))
      .withColumn("DateOnly", col("DateOnly").cast(DataTypes.StringType))
      .groupBy(col("DateOnly"), col("User"))
      .agg(Map("Message" -> "count"))
      .orderBy(col("DateOnly").desc_nulls_last)
      .withColumn("max(count(Message))", max("count(Message)").over(window))
      .filter(col("max(count(Message))") === col("count(Message)"))
      .drop(col("count(Message)"))
      .collect().toSeq.map { row =>
      val date = row.getAs[String]("DateOnly")
      val user = row.getAs[String]("User")
      val messageCount = row.getAs[Long]("max(count(Message))")
      DailyChampion(date, user, messageCount)
    }
  }

  def toModels(sourcePath: String): Seq[DailyChampion] = {
    val window = Window.partitionBy(col("DateOnly"))
    readCsv(sourcePath)
      .withColumn("DateOnly", col("Date").cast(DataTypes.DateType))
      .withColumn("DateOnly", col("DateOnly").cast(DataTypes.StringType))
      .groupBy(col("DateOnly"), col("User"))
      .agg(Map("Message" -> "count"))
      .orderBy(col("DateOnly").desc_nulls_last)
      .withColumn("max(count(Message))", max("count(Message)").over(window))
      .filter(col("max(count(Message))") === col("count(Message)"))
      .drop(col("count(Message)"))
      .collect().toSeq.map { row =>
      val date = row.getAs[String]("DateOnly")
      val user = row.getAs[String]("User")
      val messageCount = row.getAs[Long]("max(count(Message))")
      DailyChampion(date, user, messageCount)
    }
  }

  def toModels: Seq[DailyChampion] = toDf
    .collect().toSeq.map { row =>
      val date = row.getAs[String]("DateOnly")
      val user = row.getAs[String]("User")
      val messageCount = row.getAs[Long]("max(count(Message))")
      DailyChampion(date, user, messageCount)
    }

  def toDf: DataFrame = {
    val df = chatDataFrame
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
