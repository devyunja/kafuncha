package models

import akka.actor.ActorSystem

import scala.concurrent.ExecutionContext

class DatabaseContext(implicit actorSystem: ActorSystem) {
  protected implicit val databaseContext: ExecutionContext = actorSystem.dispatchers.lookup("database-context")
}
