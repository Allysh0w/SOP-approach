package com.project.sop

import akka.NotUsed
import akka.stream.scaladsl.{Flow, GraphDSL, Sink}

trait StreamCalculator {



  def sumFlowHandler2(x: Double, y: Double) = {
    x + y
  }

  def divFlowHandler(x: Double, y: Double) = {
    x * y
  }

  def diffFlowHandler(x: Double, y: Double) = {
    x - y
  }

  def multFlowHandler(x: Double, y: Double) = {
    x * y
  }

}
