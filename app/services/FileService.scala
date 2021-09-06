package services

import models.KafunchaModel
import play.api.Configuration

import java.io.{File, FileOutputStream}
import java.nio.file.{Files, Paths}
import javax.inject.{Inject, Singleton}

@Singleton
class FileService @Inject()(config: Configuration) {
  val tempFilePath: String = config.get[String]("temp-file-path")

  def isExist(path: String): Boolean = Files.exists(Paths.get(path))

  def dataToModel(bytesData: Array[Byte], kafunchaService: KafunchaService): Seq[KafunchaModel] = {
    val uuid = ChatFileUploadService.uuid
    val file = new File(s"$tempFilePath/$uuid.csv")
    val outStream = new FileOutputStream(file)

    outStream.write(bytesData)
    outStream.close()

    val models = kafunchaService.toModels(s"$tempFilePath/$uuid.csv")

    file.delete()

    models
  }
}
