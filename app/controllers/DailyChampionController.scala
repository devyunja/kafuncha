package controllers

import akka.actor.ActorSystem
import models.AnalysisContext
import services.DailyChampionService

import javax.inject._
import play.api.mvc._
import play.api.libs.json._

import scala.concurrent.Future

class DailyChampionController @Inject()(val controllerComponents: ControllerComponents,
                                        implicit val actorSystem: ActorSystem,
                                        dailyChampionService: DailyChampionService) extends AnalysisContext with BaseController {

  def dailyChampion(): Action[AnyContent] = Action.async { implicit request =>
    Future(Ok(Json.toJson(dailyChampionService.toModels)))
  }
}
