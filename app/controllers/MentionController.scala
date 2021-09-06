package controllers

import akka.actor.ActorSystem
import models.{AnalysisContext, MentionedUser}
import play.api.libs.json.Json
import play.api.mvc.{Action, AnyContent, BaseController, ControllerComponents}
import services.{AmazonS3Service, ChatFileUploadService, FileService, MentionService}

import javax.inject.{Inject, Singleton}
import scala.concurrent.Future

@Singleton
class MentionController @Inject()(implicit val actorSystem: ActorSystem,
                                  val controllerComponents: ControllerComponents,
                                  mentionService: MentionService,
                                  amazonS3Service: AmazonS3Service,
                                  fileService: FileService) extends AnalysisContext with BaseController {

  def get(filename: String): Action[AnyContent] = Action.async { implicit request => Future {
    val models = fileService
      .dataToModel(amazonS3Service.getObjectBytes(ChatFileUploadService.s3BucketName, filename), mentionService)
      .asInstanceOf[Seq[MentionedUser]]

    Ok(Json.toJson(models))
  }}
}
