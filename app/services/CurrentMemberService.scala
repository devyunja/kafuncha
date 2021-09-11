package services

import models.{CurrentMember, KafunchaModel, SparkSessionProvider}
import org.apache.spark.sql.DataFrame
import org.apache.spark.sql.functions.{col, first, max}

import javax.inject.Singleton

@Singleton
class CurrentMemberService extends SparkSessionProvider with KafunchaService {
  case class Status(date: String, user: String, status: String)

  def relatedChatMessages(sourcePath: String): DataFrame = readCsv(sourcePath)
    .filter(
      col("Message").contains("has been removed from this chatroom.") ||
      col("Message").contains("left this chatroom.") ||
      col("Message").contains("joined this chatroom."))
    .orderBy(col("Date").desc_nulls_last)

  def extractUserAndStatus(sourcePath: String): Seq[Status] = relatedChatMessages(sourcePath)
    .collect().map { row =>
      val date = row.getAs[String]("Date")
      val user = row.getAs[String]("User")
      val message = row.getAs[String]("Message")
      if (message.contains("has been removed from this chatroom.")) {
        val actualUser = message.split(" has been")
        Status(date, actualUser(0), "out")
      }
      else if (message.contains("joined this chatroom."))  Status(date, user, "in")
      else if (message.contains("left this chatroom.")) Status(date, user, "out")
      else Status(date, user, "unknown")
    }

  def outMembersMap(sourcePath: String): Map[String, Boolean] = {
    import spark.implicits._
    extractUserAndStatus(sourcePath)
      .map(status => (status.date, status.user, status.status))
      .toDF("Date", "User", "Status")
      .groupBy(col("User"))
      .agg(max(col("Date")), first(col("Status")))
      .filter(col("first(Status)") === "out")
      .collect().foldLeft(Map[String, Boolean]()){ (result, row) =>
        val user = row.getAs[String]("User")
        result ++ Map(user -> false)
      }
  }

  def outMemberDf(sourcePath: String): DataFrame = {
    import spark.implicits._
    extractUserAndStatus(sourcePath)
      .map(status => (status.date, status.user, status.status))
      .toDF("Date", "User", "Status")
      .groupBy(col("User"))
      .agg(max(col("Date")), first(col("Status")))
      .filter(col("first(Status)") === "out")
      .orderBy(col("max(Date)").desc_nulls_last)
  }

  def currentMembers(sourcePath: String): Seq[String] = {
    val outMembers = outMembersMap(sourcePath)
    readCsv(sourcePath)
      .groupBy(col("User")).count()
      .collect().map(row => row.getAs[String]("User"))
      .filter(member => outMembers.getOrElse(member, true))
  }

  def currentMembersWithAt(sourcePath: String): Seq[String] =
    currentMembers(sourcePath).map(currentMember => s"@$currentMember")

  override def toModels(sourcePath: String,
                        kafunchaServiceOption: Option[KafunchaServiceOption]): Seq[KafunchaModel] =
    Seq(CurrentMember(currentMembers(sourcePath)))
}
