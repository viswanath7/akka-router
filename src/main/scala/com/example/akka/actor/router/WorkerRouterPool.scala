package com.example.akka.actor.router

import akka.actor.{Actor, ActorRef, Props}
import com.example.akka.actor.Worker
import com.example.akka.actor.Worker.Work
import org.slf4j.LoggerFactory

object WorkerRouterPool {
  val props = Props[WorkerRouterPool]
}

/**
  * Pool: Router Actor is the parent of its routees.
  *
  * Routing allows messages to be routed to one or more actors known as routees,
  * by sending the messages to a router that will know how to route the messages to the routees.
  *
  */
class WorkerRouterPool extends Actor {

  val logger = LoggerFactory getLogger WorkerRouterPool.getClass

  var routees: List[ActorRef] = _

  override def preStart(): Unit = {
    super.preStart()
    logger debug "Creating a list of self-managed routees ..."
    routees = List.fill(5)(context.actorOf(Worker.props))
    logger debug "5 worker actor instances have been created so that messages messages may be routed to them"
  }

  override def receive: Receive = {
    case message: Work =>  logger debug s"Router $self received a 'Work' message. Forwarding the message randomly to one of its routees ..."
      routees(util.Random.nextInt(routees.size)) forward message
      // Original sender's reference is maintained even though the message goes though a mediator
  }
}
