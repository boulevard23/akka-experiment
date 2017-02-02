package com.creditkarma

import java.io.{File, FileInputStream}

import akka.actor.{Actor, ActorLogging, ActorRef, ActorSystem, Props, Terminated}
import com.creditkarma.actor.FileHandler

object WordsCounter {

  def main(args: Array[String]): Unit = {
    val system = ActorSystem("WordsCounter")

    val fileHandler = system.actorOf(Props[FileHandler], "fileHandler")

    system.actorOf(Props(classOf[Terminator], fileHandler))
  }

  class Terminator(ref: ActorRef) extends Actor with ActorLogging {
    context watch ref
    def receive = {
      case Terminated(_) =>
        log.info("{} has terminated, shutting down system", ref.path)
        context.system.terminate()
    }
  }
}
