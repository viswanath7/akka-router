package com.example.akka.application

import akka.actor.{ActorRef, ActorSystem}
import akka.routing.FromConfig
import com.example.akka.actor.Worker
import com.example.akka.actor.Worker.Work
import org.slf4j.LoggerFactory

/**
  * Demonstrates round robin router pool that loads routing logic and other settings
  * from configuration file application.conf instead of doing them programmatically
  */
object RoundRobinRouterPoolServiceApplication extends App {
  val logger = LoggerFactory getLogger RoundRobinRouterPoolServiceApplication.getClass

  logger debug "Creating the actor system ..."
  private val actorSystem: ActorSystem = ActorSystem("actor-system-round-robin-router-pool-config")

  logger debug "Creating round robin router pool by loading routing logic and settings from configuration file ..."
  val roundRobinRouterPool: ActorRef = actorSystem.actorOf(FromConfig.props(Worker.props), "round-robin-router-pool-service")

  logger debug "Sending 5 'Work' messages to the round robin router ..."
  for(count<- 1 to 5) {
    roundRobinRouterPool ! Work(s"Message: $count")
  }

  Thread sleep 100

  logger debug "Terminating actor system"
  actorSystem terminate
}
