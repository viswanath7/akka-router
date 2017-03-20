package com.example.akka.application

import akka.actor.{ActorRef, ActorSystem}
import akka.routing.FromConfig
import com.example.akka.actor.Worker
import com.example.akka.actor.Worker.Work
import org.slf4j.LoggerFactory

/**
  * Demonstrates round robin router group that loads routing logic and other settings
  * from configuration file application.conf
  */
object RoundRobinRouterGroupServiceApplication extends App {
  val logger = LoggerFactory getLogger RoundRobinRouterGroupServiceApplication.getClass

  logger debug "Creating the actor system ..."
  private val actorSystem: ActorSystem = ActorSystem("actor-system-round-robin-router-group-config")

  logger debug "Creating round robin router group by loading routing logic and settings from configuration file ..."
  val roundRobinRouterGroup: ActorRef = actorSystem.actorOf(FromConfig props, "round-robin-router-group-service")

  logger debug "Creating routee actors externally from the router ..."
  private val workers: List[ActorRef] = List.tabulate(5)(index => s"Worker$index").map(workerName=>actorSystem.actorOf(Worker.props, workerName))

  logger debug "Sending 5 'Work' messages to the round robin router ..."
  for(count<- 0 until 5) {
    roundRobinRouterGroup ! Work(s"Message: $count")
  }

  Thread sleep 100

  logger debug "Terminating actor system"
  actorSystem terminate

}
