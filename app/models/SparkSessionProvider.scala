package models

import org.apache.spark.sql.{DataFrame, SparkSession}

object SparkSessionProvider {
  private val spark = SparkSession.builder()
    .appName("Kakao Chat Analysis")
    .config("spark.master", "local")
    .getOrCreate()
}

trait SparkSessionProvider {
  protected val spark: SparkSession = SparkSessionProvider.spark

  def readChatCsv(filePath: String): DataFrame = spark.read
    .option("multiline", "true")
    .option("inferSchema", "true")
    .option("dateFormat", "MMM dd YYYY")
    .option("header", "true")
    .option("sep", ",")
    .option("nullValue", "")
    .option("quote", "\"")
    .option("escape", "\"")
    .csv(filePath)
}
