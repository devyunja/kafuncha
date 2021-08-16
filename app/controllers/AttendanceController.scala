package controllers

import akka.actor.ActorSystem
import models.AnalysisContext
import services.AttendanceService

import javax.inject._
import play.api.mvc._
import play.api.libs.json._

import scala.concurrent.Future

class AttendanceController @Inject()(val controllerComponents: ControllerComponents,
                                     implicit val actorSystem: ActorSystem,
                                     attendanceService: AttendanceService) extends AnalysisContext with BaseController {

  def attendance(): Action[AnyContent] = Action.async { implicit request =>
    Future(Ok(Json.toJson(attendanceService.toModels)))
  }
}
