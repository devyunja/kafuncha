package controllers

import services.DailyChatCountService

import javax.inject._
// import play.api._
import play.api.mvc._
import play.api.libs.json._

class DailyChatCountController @Inject()(val controllerComponents: ControllerComponents,
                                         dailyChatCountService: DailyChatCountService) extends BaseController {

  def dailyChatCount(): Action[AnyContent] = Action { implicit request: Request[AnyContent] =>
    val df = dailyChatCountService.readChatCsv("conf/chat_history.csv")
    val models = dailyChatCountService.toModels(df)
    Ok(Json.toJson(models))
  }
}
