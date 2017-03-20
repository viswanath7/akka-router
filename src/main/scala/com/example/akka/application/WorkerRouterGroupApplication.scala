package com.example.akka.application

import akka.actor.{ActorRef, ActorSystem, Props}
import com.example.akka.actor.Worker.Work
import com.example.akka.actor.Worker
import com.example.akka.actor.router.WorkerRouterGroup
import org.slf4j.LoggerFactory


object WorkerRouterGroupApplication extends App {
  val logger = LoggerFactory getLogger WorkerRouterGroupApplication.getClass

  logger info "Creating the actor system"
  private val actorSystem = ActorSystem("actor-system-worker-router-group")

  var workerNames = List.tabulate(5)(index => s"Worker$index")

  logger debug "Creating 5 workers ..."
  private val workers = workerNames.map(workerName => actorSystem.actorOf(Worker.props, workerName))

  logger debug "Creating worker router group"
  val workerRouterGroup = actorSystem.actorOf(Props(classOf[WorkerRouterGroup], workers.map(worker => worker.path.toString)))

  logger debug "Sending 5 messages to WorkerRouterGroup"
  for(count<-1 to 5) workerRouterGroup ! Work(s"Message:$count")

  Thread sleep 100

  actorSystem terminate

}
