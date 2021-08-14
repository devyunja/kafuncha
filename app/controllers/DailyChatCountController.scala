package controllers

import services.DailyChatCountService

import javax.inject._
// import play.api._
import play.api.mvc._
import play.api.libs.json._

class DailyChatCountController @Inject()(val controllerComponents: ControllerComponents,
                                         dailyChatCountService: DailyChatCountService) extends BaseController {

  def dailyChatCount(): Action[AnyContent] = Action { implicit request: Request[AnyContent] =>
    Ok(Json.toJson(dailyChatCountService.toModels))
  }
}
