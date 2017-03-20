package com.example.akka.actor

import akka.actor.{Actor, Props}
import com.example.akka.actor.Worker.Work
import org.slf4j.LoggerFactory


object Worker {
  val props = Props[Worker]
  sealed trait RequestMessage
  case class Work(message:String) extends RequestMessage
}

class Worker extends Actor {
  val logger = LoggerFactory getLogger Worker.getClass
  override def receive: Receive = {
    case Work(msg) => logger debug s"Worker actor '$self' received a 'Work' message with content '$msg'"
    case _ => logger warn s"Worker actor '$self' received unknown message type"
  }
}
