package services

import models.{KafunchaModel, MentionedUser, SparkSessionProvider}
import org.apache.spark.sql.DataFrame
import org.apache.spark.sql.functions._
import org.apache.spark.sql.types.DataTypes

import javax.inject.{Inject, Singleton}

@Singleton
class MentionService @Inject()(currentMemberService: CurrentMemberService) extends SparkSessionProvider with KafunchaService {
  override def toModels(sourcePath: String, kafunchaServiceOption: Option[KafunchaServiceOption]): Seq[KafunchaModel] = {
    toDf(sourcePath).collect().map { row =>
      val date = row.getAs[String]("DateOnly")
      val user = row.getAs[String]("User").split("@")(1)
      val mentionCount = row.getAs[Long]("MentionCount")
      MentionedUser(date, user, mentionCount)
    }
  }

  def toDf(sourcePath: String): DataFrame = {
    val df = readCsv(sourcePath)
    df.filter(col("Message").contains("@"))
      .withColumn("MessageSplit", split(col("Message"), " "))
      .select(col("*"), col("MessageSplit").getItem(0).as("OnlyID"))
      .withColumn("DateOnly", col("Date").cast(DataTypes.DateType))
      .withColumn("DateOnly", col("DateOnly").cast(DataTypes.StringType))
      .groupBy(col("DateOnly"), col("OnlyID"))
      .agg(count(col("OnlyID")).as("MentionCount"))
      .filter(col("OnlyID").isInCollection(currentMemberService.currentMembersWithAt(sourcePath)))
      .withColumnRenamed("OnlyID", "User")
      .orderBy(col("DateOnly").desc_nulls_last)
  }
}
