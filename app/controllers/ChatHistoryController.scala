package controllers

import services.ChatHistoryService

import javax.inject._
// import play.api._
import play.api.mvc._
import play.api.libs.json._

import javax.inject.Inject

@Singleton
class ChatHistoryController @Inject()(val controllerComponents: ControllerComponents,
                                      chatHistoryService: ChatHistoryService) extends BaseController {

  def chatHistory(): Action[AnyContent] = Action { implicit request: Request[AnyContent] =>
    val df = chatHistoryService.readChatCsv("conf/chat_history.csv")
    val chatHistories = chatHistoryService.toModels(df)
    Ok(Json.toJson(chatHistories))
  }
}
