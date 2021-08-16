package controllers

import akka.actor.ActorSystem
import models.AnalysisContext
import play.api.libs.json.Json
import play.api.mvc.{Action, AnyContent, BaseController, ControllerComponents, Request}
import services.LinksInChatService

import javax.inject.Inject
import scala.concurrent.Future

class LinksInChatController @Inject()(val controllerComponents: ControllerComponents,
                                      implicit val actorSystem: ActorSystem,
                                      linksInChatService: LinksInChatService) extends AnalysisContext with BaseController {

  def get(): Action[AnyContent] = Action.async { implicit request =>
    Future(Ok(Json.toJson(linksInChatService.toModels)))
  }
}
