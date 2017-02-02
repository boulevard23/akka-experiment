package com.creditkarma.actor

import akka.actor.{Actor, ActorLogging}

object WordsCountAggregator {
  case class WordsCount(count: Int)
  case object Done
}

class WordsCountAggregator extends Actor with ActorLogging{
  import  WordsCountAggregator._

  var totalCount = 0

  override def receive = {
    case WordsCount(count) =>
      totalCount += count
//      log.info(s"Counted $count words, total count is $totalCount")
    case Done =>
      log.info(s"Total words $totalCount")
      context.stop(self)
    case msg =>
      log.error("Error: message not recognized: {}", msg)
  }
}
