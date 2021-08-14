package controllers

import services.ChatHistoryService

import javax.inject._
// import play.api._
import play.api.mvc._
import play.api.libs.json._

@Singleton
class ChatHistoryController @Inject()(val controllerComponents: ControllerComponents,
                                      chatHistoryService: ChatHistoryService) extends BaseController {

  def chatHistory(): Action[AnyContent] = Action { implicit request: Request[AnyContent] =>
    Ok(Json.toJson(chatHistoryService.toModels))
  }
}
