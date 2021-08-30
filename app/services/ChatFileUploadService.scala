package services

import play.api.libs.Files

import java.io.File
import java.util.UUID

class ChatFileUploadService {
  def uuid: String = UUID.randomUUID().toString

  def upload(chatHistory: Files.TemporaryFile): String = {
    val filename = uuid
    val tempFile = File.createTempFile(s"$filename-", ".csv", new File("conf/uploads"))
    chatHistory.copyTo(tempFile, replace = true)
    tempFile.getName
  }
}
