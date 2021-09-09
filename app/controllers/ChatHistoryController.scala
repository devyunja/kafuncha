package controllers

import akka.actor.ActorSystem
import models.{AnalysisContext, ChatLine}
import services.{AmazonS3Service, ChatFileUploadService, ChatHistoryService, FileService}

import javax.inject._
import scala.concurrent.Future
import play.api.mvc._
import play.api.libs.json._

@Singleton
class ChatHistoryController @Inject()(val controllerComponents: ControllerComponents,
                                      implicit val actorSystem: ActorSystem,
                                      chatHistoryService: ChatHistoryService,
                                      fileService: FileService,
                                      amazonS3Service: AmazonS3Service) extends AnalysisContext with BaseController {

  def chatHistory(filename: String, desc: Option[Boolean]): Action[AnyContent] = Action.async { implicit request =>
    Future {
      def getModels: Seq[ChatLine] = fileService
        .dataToModel(amazonS3Service.getObjectBytes(ChatFileUploadService.s3BucketName, filename), chatHistoryService)
        .asInstanceOf[Seq[ChatLine]]

      val models = if (desc.getOrElse(false)) getModels.reverse else getModels

      Ok(Json.toJson(models))
    }
  }
}
