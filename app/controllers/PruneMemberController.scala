package controllers

import akka.actor.ActorSystem
import models.AnalysisContext
import play.api.libs.json.Json
import play.api.mvc.{Action, AnyContent, BaseController, ControllerComponents}
import services.PruneMemberService

import javax.inject.{Inject, Singleton}
import scala.concurrent.Future

@Singleton
class PruneMemberController @Inject()(val controllerComponents: ControllerComponents,
                                      implicit val actorSystem: ActorSystem,
                                      pruneMemberService: PruneMemberService) extends AnalysisContext with BaseController {

  def get(): Action[AnyContent] = Action.async { implicit request =>
    Future(Ok(Json.toJson(pruneMemberService.toModels)))
  }
}
