package controllers

import akka.actor.ActorSystem
import models.AnalysisContext
import play.api.libs.Files
import play.api.mvc.{Action, BaseController, ControllerComponents, MultipartFormData}
import services.ChatFileUploadService

import javax.inject.{Inject, Singleton}
import scala.concurrent.Future

@Singleton
class ChatFileUploadController @Inject()(val controllerComponents: ControllerComponents,
                                         implicit val actorSystem: ActorSystem,
                                         chatFileUploadService: ChatFileUploadService) extends AnalysisContext with BaseController {

  def multipart(): Action[MultipartFormData[Files.TemporaryFile]] = Action.async(parse.multipartFormData) { request =>
    Future {
      request.body.file("chat_history").map { chatHistory => Ok(chatFileUploadService.upload(chatHistory.ref)) }
        .getOrElse(BadRequest("missing file"))
    }
  }
}
