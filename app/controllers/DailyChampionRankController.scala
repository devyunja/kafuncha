package controllers

import akka.actor.ActorSystem
import models.{AnalysisContext, DailyChampion, DailyChampionRank}
import play.api.libs.json.Json
import play.api.mvc.{Action, AnyContent, BaseController, ControllerComponents}
import services.{AmazonS3Service, ChatFileUploadService, DailyChampionRankService, FileService}

import javax.inject.{Inject, Singleton}
import scala.concurrent.Future

@Singleton
class DailyChampionRankController @Inject()(val controllerComponents: ControllerComponents,
                                            implicit val actorSystem: ActorSystem,
                                            dailyChampionRankService: DailyChampionRankService,
                                            amazonS3Service: AmazonS3Service,
                                            fileService: FileService) extends AnalysisContext with BaseController {

  def get(filename: String): Action[AnyContent] = Action.async { implicit request => Future {
    val models = fileService
      .dataToModel(amazonS3Service.getObjectBytes(ChatFileUploadService.s3BucketName, filename), dailyChampionRankService)
      .asInstanceOf[Seq[DailyChampionRank]]

    Ok(Json.toJson(models))
  }}
}
