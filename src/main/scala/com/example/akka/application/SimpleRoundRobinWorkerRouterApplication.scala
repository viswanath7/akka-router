package com.example.akka.application

import akka.actor.{ActorRef, ActorSystem}
import com.example.akka.actor.Worker.Work
import com.example.akka.actor.router.SimpleRoundRobinWorkerRouter
import org.slf4j.LoggerFactory


object SimpleRoundRobinWorkerRouterApplication extends App {

  val logger = LoggerFactory getLogger SimpleRoundRobinWorkerRouterApplication.getClass

  logger info "Creating the actor system"
  private val actorSystem: ActorSystem = ActorSystem("actor-system-worker-simple-round-robin-router")

  logger info "Creating a SimpleRoundRobinWorkerRouter actor"
  private val simpleRoundRobinWorkerRouter: ActorRef = actorSystem.actorOf(SimpleRoundRobinWorkerRouter.props)

  logger debug "Sending 4 'Work' messages to SimpleRoundRobinWorkerRouter ..."
  for( count <- 1 until 5) {
    simpleRoundRobinWorkerRouter ! Work(s"Message: $count")
  }

  Thread sleep 100

  logger debug "Terminating actor system"
  actorSystem terminate

}
