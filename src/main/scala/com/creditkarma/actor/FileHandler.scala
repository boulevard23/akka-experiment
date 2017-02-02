package com.creditkarma.actor

import java.io.{File, FileInputStream}

import akka.actor.{Actor, ActorLogging, ActorRef, Props}

class FileHandler extends Actor with ActorLogging {

  lazy val dir = "src/main/resources"
  var totalCount = 0
  var processedCount = 0
  var countAggregator: ActorRef = _

  @scala.throws[Exception](classOf[Exception])
  override def preStart(): Unit = {
    countAggregator = context.actorOf(Props[WordsCountAggregator], "countAggregator")

    getFiles(dir).foreach { file =>
      context.actorOf(Props[FileProcessor]) ! FileProcessor.ProcessInputStream(new FileInputStream(file), file.getName)
      totalCount += 1
    }
  }

  override def receive = {
    case FileProcessor.Done(in, name) =>
      in.close()
      processedCount += 1
      log.info(s"$name is processed")
      if (processedCount == totalCount) {
        countAggregator ! WordsCountAggregator.Done
        context.stop(self)
      }
  }

  def getFiles(dir: String): List[File] = {
    val d = new File(dir)
    if (d.exists && d.isDirectory) {
      d.listFiles.filter(_.isFile).toList
    } else {
      List[File]()
    }
  }
}
