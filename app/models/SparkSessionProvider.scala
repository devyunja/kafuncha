package models

import org.apache.spark.sql.SparkSession

object SparkSessionProvider {
  private val spark = SparkSession.builder()
    .appName("Kakao Chat Analysis")
    .config("spark.master", "local")
    .getOrCreate()
}

trait SparkSessionProvider {
  protected val spark: SparkSession = SparkSessionProvider.spark
}
