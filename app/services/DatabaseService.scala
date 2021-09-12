package services

import akka.actor.ActorSystem
import models.DatabaseContext
import play.api.db.Database
import play.api.Logger

import javax.inject.{Inject, Singleton}
import scala.concurrent.Future

@Singleton
class DatabaseService @Inject()(database: Database, implicit val actorSystem: ActorSystem) extends DatabaseContext {
  val logger: Logger = Logger(this.getClass)

  def connectionTest(): Unit = {
    Future {
      database.withConnection { connection =>
        logger.info(s"JDBC driver connected: $connection")
      }
    }
  }
}
