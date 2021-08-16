package controllers

import akka.actor.ActorSystem
import models.AnalysisContext
import services.ChatHistoryService

import javax.inject._
import scala.concurrent.Future
import play.api.mvc._
import play.api.libs.json._

@Singleton
class ChatHistoryController @Inject()(val controllerComponents: ControllerComponents,
                                      implicit val actorSystem: ActorSystem,
                                      chatHistoryService: ChatHistoryService) extends AnalysisContext with BaseController {

  def chatHistory(): Action[AnyContent] = Action.async { implicit request =>
    Future(Ok(Json.toJson(chatHistoryService.toModels)))
  }

  def chatHistoryPaged(page: Int, offset: Int): Action[AnyContent] = Action.async { implicit request =>
    Future(Ok(Json.toJson(chatHistoryService.toModels(page, offset))))
  }
}
