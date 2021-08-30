package models

import org.apache.spark.sql.{DataFrame, SparkSession}

object SparkSessionProvider {
  private val spark = SparkSession.builder()
    .appName("Kakao Chat Analysis")
    .config("spark.master", "local")
    .getOrCreate()

  private val chatDataFrame = readChatCsv()

  private def readChatCsv(): DataFrame = spark.read
    .option("multiline", "true")
    .option("inferSchema", "true")
    .option("dateFormat", "MMM dd YYYY")
    .option("header", "true")
    .option("sep", ",")
    .option("nullValue", "")
    .option("quote", "\"")
    .option("escape", "\"")
    .csv("conf/chat_history.csv")
}

trait SparkSessionProvider {
  val spark: SparkSession = SparkSessionProvider.spark
  val chatDataFrame: DataFrame = SparkSessionProvider.chatDataFrame

  protected def readCsv(path: String): DataFrame = spark.read
    .option("multiline", "true")
    .option("inferSchema", "true")
    .option("dateFormat", "MMM dd YYYY")
    .option("header", "true")
    .option("sep", ",")
    .option("nullValue", "")
    .option("quote", "\"")
    .option("escape", "\"")
    .csv(path)
}
