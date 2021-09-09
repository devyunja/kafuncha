package services

import play.api.Configuration
import play.api.libs.Files

import java.io.File
import java.util.UUID
import javax.inject.{Inject, Singleton}

object ChatFileUploadService {
  val s3BucketName = "kafuncha-chat-history-upload"
  def uuid: String = UUID.randomUUID().toString
}

@Singleton
class ChatFileUploadService @Inject()(amazonS3Service: AmazonS3Service, config: Configuration) {
  import ChatFileUploadService._

  val configTempFilePath: String = config.get[String]("temp-file-path")

  def upload(chatHistory: Files.TemporaryFile): String = {
    val filename = uuid
    val tempFile = File.createTempFile(s"$filename-", ".csv", new File(configTempFilePath))
    val tempFilename = tempFile.getName
    val tempFilePath = tempFile.getPath

    chatHistory.copyTo(tempFile, replace = true)

    if (!amazonS3Service.findBucket(s3BucketName)) amazonS3Service.createBucket(s3BucketName)
    amazonS3Service.uploadObject(s3BucketName, tempFilePath, tempFilename, FileMimeType.CSV)

    tempFile.delete()

    tempFilename
  }
}
