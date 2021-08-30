package services

import java.nio.file.{Files, Paths}
import javax.inject.Singleton

@Singleton
class FileService {
  def isExist(path: String): Boolean = Files.exists(Paths.get(path))
}
