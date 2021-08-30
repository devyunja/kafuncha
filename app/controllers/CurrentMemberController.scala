package controllers

import akka.actor.ActorSystem
import models.AnalysisContext
import play.api.libs.json.Json
import play.api.mvc.{Action, AnyContent, BaseController, ControllerComponents}
import services.CurrentMemberService

import javax.inject.{Inject, Singleton}
import scala.concurrent.Future

@Singleton
class CurrentMemberController @Inject()(implicit val actorSystem: ActorSystem,
                                        val controllerComponents: ControllerComponents,
                                        currentMemberService: CurrentMemberService) extends AnalysisContext with BaseController {

  def get(): Action[AnyContent] = Action.async { implicit request =>
    Future(Ok(Json.toJson(currentMemberService.currentMembers)))
  }
}
