package controllers

import akka.actor.ActorSystem
import models.AnalysisContext
import services.DailyChatCountService

import javax.inject._
import play.api.mvc._
import play.api.libs.json._

import scala.concurrent.Future

class DailyChatCountController @Inject()(val controllerComponents: ControllerComponents,
                                         implicit val actorSystem: ActorSystem,
                                         dailyChatCountService: DailyChatCountService) extends AnalysisContext with BaseController {

  def dailyChatCount(): Action[AnyContent] = Action.async { implicit request =>
    Future(Ok(Json.toJson(dailyChatCountService.toModels)))
  }
}
