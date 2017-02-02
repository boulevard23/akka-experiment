package com.creditkarma.actor

import akka.actor.{Actor, ActorLogging, ActorSystem}

object Incrementor {
  case object IncrementFileCounter
}

class Incrementor(totalNumOfFiles: Int, system: ActorSystem) extends Actor with ActorLogging {
  import Incrementor._
  private var fileCounter = 0

  override def receive = {
    case IncrementFileCounter =>
      fileCounter += 1
      if (totalNumOfFiles == fileCounter) system.terminate()
    case msg => log.error("Error: message not recognized: {}", msg)
  }
}
