package com.creditkarma.actor

import java.io.InputStream

import akka.actor.{Actor, ActorLogging, Props}

import scala.io.Source

object FileProcessor {

  case class ProcessInputStream(in: InputStream, name: String)

  case object LineProcessed

  case class Done(in: InputStream, name: String)
}

class FileProcessor extends Actor with ActorLogging {
  import FileProcessor._

  var totalLines = 0
  var processedLines = 0
  var totalWords = 0
  var inputStream: InputStream = _
  var fileName: String = ""

  override def receive = {
    case ProcessInputStream(in, name) => {
      inputStream = in
      fileName = name
      val lines = Source.fromInputStream(in).getLines()

      val counter = context.actorOf(Props[WordsCountAggregator])
      lines.foreach { line =>
        totalLines += 1
        context.actorOf(Props[LineProcessor]) ! LineProcessor.ProcessLine(line)
      }
    }
    case LineProcessed =>
//      log.info(s"$fileName processed $processedLines lines")
      processedLines += 1
      if (processedLines == totalLines) {
        log.info("Lines are all processed")
        context.actorSelection("..") ! Done(inputStream, fileName)
      }
    case msg =>
      log.error("Error: message not recognized: {}", msg)
  }
}
