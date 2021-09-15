package controllers

import akka.actor.ActorSystem
import models.{AnalysisContext, DailyChatCount}
import services.{AmazonS3Service, ChatFileUploadService, DailyChatCountService, FileService}

import javax.inject._
import play.api.mvc._
import play.api.libs.json._

import scala.concurrent.Future

@Singleton
class DailyChatCountController @Inject()(val controllerComponents: ControllerComponents,
                                         implicit val actorSystem: ActorSystem,
                                         dailyChatCountService: DailyChatCountService,
                                         fileService: FileService,
                                         amazonS3Service: AmazonS3Service) extends AnalysisContext with BaseController {

  def dailyChatCount(filename: String): Action[AnyContent] = Action.async { implicit request =>
    Future {
      val models = fileService
        .dataToModel(amazonS3Service.getObjectBytes(ChatFileUploadService.s3BucketName, filename), dailyChatCountService)
        .asInstanceOf[Seq[DailyChatCount]]

      Ok(Json.toJson(models))
    }
  }
}
