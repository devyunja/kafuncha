package controllers

import akka.actor.ActorSystem
import models.AnalysisContext
import play.api.libs.json.Json
import play.api.mvc.{Action, AnyContent, BaseController, ControllerComponents}
import services.KeywordService

import javax.inject.{Inject, Singleton}
import scala.concurrent.Future

@Singleton
class KeywordController @Inject()(val controllerComponents: ControllerComponents,
                                  implicit val actorSystem: ActorSystem,
                                  keywordService: KeywordService) extends AnalysisContext with BaseController {

  def today(): Action[AnyContent] = Action.async { implicit request =>
    Future(Ok(Json.toJson(keywordService.todayKeywordCount)))
  }

  def byDate(date: String): Action[AnyContent] = Action.async { implicit request =>
    Future(Ok(Json.toJson(keywordService.keywordCountByDate(date))))
  }

  def byDates(dates: Seq[String]): Action[AnyContent] = Action.async { implicit request =>
    Future(Ok(Json.toJson(keywordService.bulkKeywordCountByDates(dates))))
  }
}
