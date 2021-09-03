package controllers

import akka.actor.ActorSystem
import models.{AnalysisContext, DailyChampion}
import services.{AmazonS3Service, ChatFileUploadService, DailyChampionService, FileService}

import javax.inject._
import play.api.mvc._
import play.api.libs.json._

import scala.concurrent.Future

@Singleton
class DailyChampionController @Inject()(val controllerComponents: ControllerComponents,
                                        implicit val actorSystem: ActorSystem,
                                        dailyChampionService: DailyChampionService,
                                        amazonS3Service: AmazonS3Service,
                                        fileService: FileService) extends AnalysisContext with BaseController {

  def dailyChampion(filename: String): Action[AnyContent] = Action.async { implicit request => Future {
    val models = fileService
      .dataToModel(amazonS3Service.getObjectBytes(ChatFileUploadService.s3BucketName, filename), dailyChampionService)
      .asInstanceOf[Seq[DailyChampion]]

    Ok(Json.toJson(models))
  }}
}
