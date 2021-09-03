package services

import models.KafunchaModel

trait KafunchaService {
  def toModel(sourcePath: String): Seq[KafunchaModel]
}
