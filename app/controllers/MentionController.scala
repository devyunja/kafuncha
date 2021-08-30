package controllers

import akka.actor.ActorSystem
import models.AnalysisContext
import play.api.mvc.{Action, AnyContent, BaseController, ControllerComponents}
import services.MentionService

import javax.inject.{Inject, Singleton}
import scala.concurrent.Future

@Singleton
class MentionController @Inject()(implicit val actorSystem: ActorSystem,
                                  val controllerComponents: ControllerComponents,
                                  mentionService: MentionService) extends AnalysisContext with BaseController {

  def get(): Action[AnyContent] = Action.async { implicit request =>
    Future(Ok("GET /mention"))
  }
}
