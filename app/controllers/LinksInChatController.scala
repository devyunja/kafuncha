package controllers

import play.api.libs.json.Json
import play.api.mvc.{Action, AnyContent, BaseController, ControllerComponents, Request}
import services.LinksInChatService

import javax.inject.Inject

class LinksInChatController @Inject()(val controllerComponents: ControllerComponents,
                                      linksInChatService: LinksInChatService) extends BaseController {

  def get(): Action[AnyContent] = Action { implicit request: Request[AnyContent] =>
    Ok(Json.toJson(linksInChatService.toModels))
  }
}
