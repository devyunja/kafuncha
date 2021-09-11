package services

import models.{KafunchaModel, PruneMember, SparkSessionProvider}
import org.apache.spark.sql.{Column, DataFrame}
import org.apache.spark.sql.functions.{col, current_date, datediff, max}
import org.apache.spark.sql.types.DataTypes

import javax.inject.{Inject, Singleton}

@Singleton
class PruneMemberService @Inject()(currentMemberService: CurrentMemberService) extends SparkSessionProvider with KafunchaService {
  def toModels(sourcePath: String): Seq[PruneMember] = currentUserLastChat(sourcePath)
    .withColumn("LastShowDate", col("LastShowDate").cast(DataTypes.StringType))
    .withColumn("Today", col("Today").cast(DataTypes.StringType))
    .collect().map { row =>
      val user = row.getAs[String]("User")
      val lastShowDate = row.getAs[String]("LastShowDate")
      val today = row.getAs[String]("Today")
      val dateDiff = row.getAs[Int]("DateDiff")
      PruneMember(user, lastShowDate, today, dateDiff)
    }

  def allUsersLastChat(sourcePath: String): DataFrame = readCsv(sourcePath)
    .withColumn("DateOnly", col("Date").cast(DataTypes.DateType))
    .withColumn("Today", current_date())
    .groupBy(col("User"))
    .agg(max(col("DateOnly")).as("LastShowDate"), max(col("Today")).as("Today"))
    .withColumn("DateDiff", datediff(col("Today"), col("LastShowDate")))
    .orderBy(col("DateDiff").desc_nulls_last)

  def currentUserLastChat(sourcePath: String): DataFrame = {
    val currentMembers = currentMemberService.currentMembers(sourcePath)
    def orFilterGeneratorMultiContains(filterPredicates: Seq[String], column: String): Column = {
      val coi = col(column)
      filterPredicates.map(coi.contains).reduce(_ || _)
    }
    allUsersLastChat(sourcePath).filter(orFilterGeneratorMultiContains(currentMembers, "User"))
  }

  override def toModels(sourcePath: String,
                        kafunchaServiceOption: Option[KafunchaServiceOption]): Seq[KafunchaModel] =
    toModels(sourcePath)
}
