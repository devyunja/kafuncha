package services

import models.{LinksInChat, SparkSessionProvider}
import org.apache.spark.sql.DataFrame
import org.apache.spark.sql.functions.col

import java.util.regex.Pattern
import javax.inject.Singleton

@Singleton
class LinksInChatService extends SparkSessionProvider {
  def toModels: Seq[LinksInChat] = toDf
    .collect().toSeq.map { row =>
      val date = row.getAs[String]("Date")
      val user = row.getAs[String]("User")
      val message = row.getAs[String]("Message")
      LinksInChat(date, user, seqToString(extractUrl(message)))
    }

  def toDf: DataFrame = chatDataFrame
    .filter(col("User") =!= "방장봇")
    .filter(col("Message").contains("https://"))

  def extractUrl(message: String): Seq[String] = {
    val regex = "\\b((?:https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:, .;]*[-a-zA-Z0-9+&@#/%=~_|])"
    val pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE)
    val matcher = pattern.matcher(message)
    var links = Seq[String]()
    while (matcher.find()) {
      links = message.substring(matcher.start(0), matcher.end(0)) +: links
    }
    links
  }

  def seqToString(strings: Seq[String]): String =
    if (strings.size < 2) strings.head
    else strings.foldLeft("") { (result, current) =>
      if (result == "") current
      else s"$result, $current"
    }
}
