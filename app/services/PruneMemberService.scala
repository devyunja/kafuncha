package services

import models.{PruneMember, SparkSessionProvider}
import org.apache.spark.sql.{Column, DataFrame}
import org.apache.spark.sql.functions.{col, current_date, datediff, max}
import org.apache.spark.sql.types.DataTypes

import javax.inject.Inject

class PruneMemberService @Inject()(currentMemberService: CurrentMemberService) extends SparkSessionProvider {
  def toModels: Seq[PruneMember] = currentUserLastChat
    .withColumn("LastShowDate", col("LastShowDate").cast(DataTypes.StringType))
    .withColumn("Today", col("Today").cast(DataTypes.StringType))
    .collect().map { row =>
      val user = row.getAs[String]("User")
      val lastShowDate = row.getAs[String]("LastShowDate")
      val today = row.getAs[String]("Today")
      val dateDiff = row.getAs[Int]("DateDiff")
      PruneMember(user, lastShowDate, today, dateDiff)
    }

  def allUsersLastChat: DataFrame = chatDataFrame
    .withColumn("DateOnly", col("Date").cast(DataTypes.DateType))
    .withColumn("Today", current_date())
    .groupBy(col("User"))
    .agg(max(col("DateOnly")).as("LastShowDate"), max(col("Today")).as("Today"))
    .withColumn("DateDiff", datediff(col("Today"), col("LastShowDate")))
    .orderBy(col("DateDiff").desc_nulls_last)

  def currentUserLastChat: DataFrame = {
    val currentMembers = currentMemberService.currentMembers
    def orFilterGeneratorMultiContains(filterPredicates: Seq[String], column: String): Column = {
      val coi = col(column)
      filterPredicates.map(coi.contains).reduce(_ || _)
    }
    allUsersLastChat.filter(orFilterGeneratorMultiContains(currentMembers, "User"))
  }
}
