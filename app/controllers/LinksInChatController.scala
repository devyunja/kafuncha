package controllers

import play.api.libs.json.Json
import play.api.mvc.{Action, AnyContent, BaseController, ControllerComponents, Request}
import services.LinksInChatService

import javax.inject.Inject

class LinksInChatController @Inject()(val controllerComponents: ControllerComponents,
                                      linksInChatService: LinksInChatService) extends BaseController {

  def get(): Action[AnyContent] = Action { implicit request: Request[AnyContent] =>
    val df = linksInChatService.readChatCsv("conf/chat_history.csv")
    val models = linksInChatService.toModels(df)
    Ok(Json.toJson(models))
  }
}
