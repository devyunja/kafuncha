package controllers

import akka.actor.ActorSystem
import models.{AnalysisContext, PruneMember}
import play.api.libs.json.Json
import play.api.mvc.{Action, AnyContent, BaseController, ControllerComponents}
import services.{AmazonS3Service, ChatFileUploadService, FileService, PruneMemberService}

import javax.inject.{Inject, Singleton}
import scala.concurrent.Future

@Singleton
class PruneMemberController @Inject()(val controllerComponents: ControllerComponents,
                                      implicit val actorSystem: ActorSystem,
                                      pruneMemberService: PruneMemberService,
                                      amazonS3Service: AmazonS3Service,
                                      fileService: FileService) extends AnalysisContext with BaseController {

  def get(filename: String): Action[AnyContent] = Action.async { implicit request =>
    Future {
      val models = fileService
        .dataToModel(
          amazonS3Service.getObjectBytes(ChatFileUploadService.s3BucketName, filename),
          pruneMemberService)
        .asInstanceOf[Seq[PruneMember]]

      Ok(Json.toJson(models))
    }
  }
}
