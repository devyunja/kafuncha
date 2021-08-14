package services

import models.{Attendance, SparkSessionProvider}
import org.apache.spark.sql.DataFrame
import org.apache.spark.sql.functions._

class AttendanceService extends SparkSessionProvider {
  def toModels: Seq[Attendance] = chatDataFrame
    .groupBy(col("User"))
    .count.orderBy(col("count").desc_nulls_last).collect().toSeq.map { row =>
      val user = row.getAs[String]("User")
      val count = row.getAs[Long]("count")
      Attendance(user, count)
    }
}
