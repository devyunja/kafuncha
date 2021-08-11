package controllers

import services.AttendanceService

import javax.inject._
// import play.api._
import play.api.mvc._
import play.api.libs.json._

class AttendanceController @Inject()(val controllerComponents: ControllerComponents,
                                     attendanceService: AttendanceService) extends BaseController {

  def attendance(): Action[AnyContent] = Action { implicit request: Request[AnyContent] =>
    val df = attendanceService.readChatCsv("conf/chat_history.csv")
    val attendances = attendanceService.toModels(df)
    Ok(Json.toJson(attendances))
  }
}
