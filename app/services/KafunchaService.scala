package services

import models.KafunchaModel

trait KafunchaService {
  def toModels(sourcePath: String): Seq[KafunchaModel]
}
