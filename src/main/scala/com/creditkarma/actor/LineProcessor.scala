package com.creditkarma.actor

import akka.actor.{Actor, ActorLogging, ActorRef, Props}

object LineProcessor {

  case class ProcessLine(line: String)
}

class LineProcessor extends Actor with ActorLogging {
  import LineProcessor._

  override def receive = {
    case ProcessLine(line) =>
      val wordsCount = line.split(" ").length
      context.actorSelection("/user/fileHandler/countAggregator") ! WordsCountAggregator.WordsCount(wordsCount)
      sender() ! FileProcessor.LineProcessed
    case msg =>
      log.error("Error: message not recognized: {}", msg)
  }
}
