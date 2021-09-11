package services

import models.KafunchaModel

case class KafunchaServiceOption(desc: Option[Boolean] = None,
                                 rewindNumDays: Option[Int] = None)

trait KafunchaService {
  def toModels(sourcePath: String, kafunchaServiceOption: Option[KafunchaServiceOption]): Seq[KafunchaModel]
}
