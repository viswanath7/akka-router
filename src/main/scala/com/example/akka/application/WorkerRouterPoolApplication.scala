package com.example.akka.application

import akka.actor.{ActorRef, ActorSystem}
import com.example.akka.actor.Worker.Work
import com.example.akka.actor.router.WorkerRouterPool
import org.slf4j.LoggerFactory


object WorkerRouterPoolApplication extends App {

  val logger = LoggerFactory getLogger WorkerRouterPoolApplication.getClass

  logger info "Creating the actor system"
  private val actorSystem: ActorSystem = ActorSystem("actor-system-worker-router-pool")

  logger info "Creating a WorkerRouterPool actor"
  private val workerRouter: ActorRef = actorSystem.actorOf(WorkerRouterPool.props)

  logger debug "Sending 4 'Work' messages to WorkerRouterPool ..."
  for( count <- 1 until 5) {
    workerRouter ! Work(s"Message: $count")
  }

  Thread sleep 100

  logger debug "Terminating actor system"
  actorSystem terminate
}
