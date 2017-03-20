package com.example.akka.actor.router

import akka.actor.Actor
import com.example.akka.actor.Worker.Work
import org.slf4j.LoggerFactory

import scala.util.Random


class WorkerRouterGroup(routeesPath: List[String]) extends Actor {
  val logger = LoggerFactory getLogger WorkerRouterGroup.getClass

  override def receive: Receive = {
    case message: Work => logger debug s"Router '$self' forwarding a 'Work' message to one of its routees ..."
      context.actorSelection(routeesPath(Random.nextInt(routeesPath.size))) forward message
    case _ => logger warn s"Router '$self': Unsupported message type!"
  }
}

object WorkerRouterGroup
