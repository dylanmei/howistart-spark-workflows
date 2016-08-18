package howistart

import org.apache.spark._
import scala.math.random

object TcpDump {
  def main(args: Array[String]) {
    TcpDump.run(1)
  }

  def run(slices: Int): Unit = {
    val conf = new SparkConf().setAppName("TcpDump Batch")
    val sc = new SparkContext(conf)

    val n = math.min(100000L * slices, Int.MaxValue).toInt // avoid overflow
    val count = sc.parallelize(1 until n, slices).map { i =>
      val x = random * 2 - 1
      val y = random * 2 - 1
      if (x*x + y*y < 1) 1 else 0
    }.reduce(_ + _)

    println("Pi is roughly " + 4.0 * count / n)
    println("Hello Spark!")
    sc.stop()
  }
}

