package services

import java.nio.file.{Files, Paths}

class FileService {
  def isExist(path: String): Boolean = Files.exists(Paths.get(path))
}
