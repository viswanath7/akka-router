package com.example.akka.actor.router

import akka.actor.{Actor, ActorRef, Props, Terminated}
import akka.routing.{ActorRefRoutee, RoundRobinRoutingLogic, Router}
import com.example.akka.actor.Worker
import com.example.akka.actor.Worker.Work
import org.slf4j.LoggerFactory

object SimpleRoundRobinWorkerRouter {
  val props = Props[SimpleRoundRobinWorkerRouter]
}

class SimpleRoundRobinWorkerRouter extends Actor {

  val logger = LoggerFactory getLogger SimpleRoundRobinWorkerRouter.getClass

  def generateActorRefRoutee(workerName: String): ActorRefRoutee = {
    val workerActorRef: ActorRef = context.actorOf(Worker.props, workerName)
    context watch workerActorRef // Register SimpleRoundRobinWorkerRouter as a monitor for Worker
    ActorRefRoutee(workerActorRef)
  }

  val router: Router = {
    logger debug "Creating a router with round robin routing logic ..."
    val routees = Vector.tabulate(5)(index => s"Worker$index")
      .map(workerName => generateActorRefRoutee(workerName))
    Router(RoundRobinRoutingLogic(), routees)
  }

  override def receive: Receive = {
    case message:Work => logger debug s"Router $self received a 'Work' message so forwarding it to routees"
      router.route(message, sender())
    case Terminated(workerRoutee) => logger info s"Routee $workerRoutee was terminated!"
  }
}
