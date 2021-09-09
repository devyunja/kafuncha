package controllers

import akka.actor.ActorSystem
import models.{AnalysisContext, DailyChampionRank}
import play.api.Configuration
import play.api.libs.json.Json
import play.api.mvc.{Action, AnyContent, BaseController, ControllerComponents}
import services.{AmazonS3Service, ChatFileUploadService, DailyChampionRankService, FileService}

import java.io.{File, FileOutputStream}
import javax.inject.{Inject, Singleton}
import scala.concurrent.Future

@Singleton
class DailyChampionRankController @Inject()(val controllerComponents: ControllerComponents,
                                            implicit val actorSystem: ActorSystem,
                                            dailyChampionRankService: DailyChampionRankService,
                                            amazonS3Service: AmazonS3Service,
                                            fileService: FileService,
                                            config: Configuration) extends AnalysisContext with BaseController {

  val tempFilePath: String = config.get[String]("temp-file-path")

  def get(filename: String, rewindNumDays: Option[Int]): Action[AnyContent] = Action.async { implicit request => Future {
    if (rewindNumDays.isDefined) {
      val byteArray = amazonS3Service.getObjectBytes(ChatFileUploadService.s3BucketName, filename)
      val uuid = ChatFileUploadService.uuid
      val file = new File(s"$tempFilePath/$uuid.csv")
      val outStream = new FileOutputStream(file)

      outStream.write(byteArray)
      outStream.close()

      val models = dailyChampionRankService.toModels(s"$tempFilePath/$uuid.csv", rewindNumDays.get)

      file.delete()

      Ok(Json.toJson(models))
    }
    else {
      val models = fileService
        .dataToModel(amazonS3Service.getObjectBytes(ChatFileUploadService.s3BucketName, filename), dailyChampionRankService)
        .asInstanceOf[Seq[DailyChampionRank]]

      Ok(Json.toJson(models))
    }
  }}
}
