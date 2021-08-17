package services

import models.SparkSessionProvider
import org.apache.spark.sql.DataFrame
import org.apache.spark.sql.functions.{col, first, max}

class CurrentMemberService extends SparkSessionProvider {
  case class Status(date: String, user: String, status: String)

  private def relatedChatMessages: DataFrame = chatDataFrame
    .filter(
      col("Message").contains("has been removed from this chatroom.") ||
      col("Message").contains("left this chatroom.") ||
      col("Message").contains("joined this chatroom."))
    .orderBy(col("Date").desc_nulls_last)

  private def extractUserAndStatus: Seq[Status] = relatedChatMessages
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

  private def outMembersMap: Map[String, Boolean] = {
    import spark.implicits._
    extractUserAndStatus
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

  def currentMembers: Seq[String] = {
    val outMembers = outMembersMap
    chatDataFrame
      .groupBy(col("User")).count()
      .collect().map(row => row.getAs[String]("User"))
      .filter(member => outMembers.getOrElse(member, true))
  }
}
