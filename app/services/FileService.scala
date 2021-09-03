package services

import models.KafunchaModel

import java.io.{File, FileOutputStream}
import java.nio.file.{Files, Paths}
import javax.inject.Singleton

object FileService {
  val uploadPath = "/opt/docker/conf"
}

@Singleton
class FileService {
  import FileService._

  def isExist(path: String): Boolean = Files.exists(Paths.get(path))

  def dataToModel(bytesData: Array[Byte], kafunchaService: KafunchaService): Seq[KafunchaModel] = {
    val uuid = ChatFileUploadService.uuid
    val file = new File(s"$uploadPath/$uuid.csv")
    val outStream = new FileOutputStream(file)

    outStream.write(bytesData)
    outStream.close()

    val models = kafunchaService.toModels(s"$uploadPath/$uuid.csv")

    file.delete()

    models
  }
}
