package models

import akka.actor.ActorSystem

import scala.concurrent.ExecutionContext

class AnalysisContext(implicit actorSystem: ActorSystem) {
  protected implicit val analysisContext: ExecutionContext = actorSystem.dispatchers.lookup("analysis-context")
}
