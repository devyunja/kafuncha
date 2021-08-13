package controllers

import services.DailyChampionService

import javax.inject._
// import play.api._
import play.api.mvc._
import play.api.libs.json._

class DailyChampionController @Inject()(val controllerComponents: ControllerComponents,
                                        dailyChampionService: DailyChampionService) extends BaseController {

  def dailyChampion(): Action[AnyContent] = Action { implicit request: Request[AnyContent] =>
    val df = dailyChampionService.readChatCsv("conf/chat_history.csv")
    val models = dailyChampionService.toModels(df)
    Ok(Json.toJson(models))
  }
}
