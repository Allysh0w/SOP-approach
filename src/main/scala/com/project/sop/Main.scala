package com.project.sop

import akka.NotUsed
import akka.actor.ActorSystem
import akka.stream.{ActorMaterializer, ClosedShape, FlowShape, Inlet, UniformFanOutShape}
import akka.stream.scaladsl.{Balance, Broadcast, Flow, GraphDSL, Merge, RunnableGraph, Sink, Source, Zip}

import scala.io.StdIn

object Main extends App with StreamCalculator {


  println("Choose an operation: ")
  println(s"Choose an operation: \n" +
    s"1 - Sum\n" +
    s"2 - Diff\n" +
    s"3 - Mult\n" +
    s"4 - Div\n")

  implicit val system = ActorSystem()
  implicit val ec = system.dispatcher

  //just for test values
  val operation = 2
  val inputValue1: Double = 1
  val inputValue2: Double = 2

  val g = GraphDSL.create(Sink.head[Double]) { implicit b =>
    sink =>

      import GraphDSL.Implicits._

      operation match {
        case 1 =>
          Source.single(inputValue1) ~> b.add(Flow.fromFunction(x => sumFlowHandler2(x, inputValue2))) ~> sink

        case 2 =>
//          val maxSink: Sink[Int, Future[Int]] = Flow[Int].toMat(Sink.fold(0)((acc, elem) => math.max(acc, elem)))(Keep.right)
//          val broadcast: UniformFanOutShape[Double, Double] = b.add(Broadcast[Double](2))
          val broadcast = b.add(Broadcast[Double](2))
          val zip = b.add(Zip[Double, Double])
//          val merge = b.add(Merge[Double](2))
          val flowx: Flow[(Double, Double), Double, NotUsed] = Flow[(Double, Double)].map(x => x._1 + x._2)

//          val flowsum: FlowShape[Double, Double] = b.add(Flow[Double].map(x => x + x))

          Source.single(inputValue1) ~>
            b.add(Flow.fromFunction(x => sumFlowHandler2(x, inputValue2))) ~> broadcast.in
            broadcast.out(0) ~> b.add(Flow[Double].map(x => x + 1)) ~> zip.in0
            broadcast.out(1) ~> b.add(Flow[Double].map(x => x + 10)) ~> zip.in1

          zip.out ~> flowx ~> sink



        //          broadcast.out(0) ~> merge.in(0)
//          broadcast.out(1) ~> merge.in(1)
//          merge ~> flowsum ~> sink

      }

      ClosedShape
  }

  RunnableGraph.fromGraph(g).run().foreach(println)

}

