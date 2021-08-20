package controllers

import akka.actor.ActorSystem
import models.AnalysisContext
import play.api.libs.json.Json
import play.api.mvc.{Action, AnyContent, BaseController, ControllerComponents}
import services.KeyWordService

import javax.inject.Inject
import scala.concurrent.Future

class KeyWordController @Inject()(val controllerComponents: ControllerComponents,
                                  implicit val actorSystem: ActorSystem,
                                  keyWordService: KeyWordService) extends AnalysisContext with BaseController {

  def getDaily(): Action[AnyContent] = Action.async { implicit request =>
    Future(Ok(Json.toJson(keyWordService.dailyKeyWordCount)))
  }
}
