package howistart.streaming

import org.apache.spark._
import org.apache.spark.streaming._
import org.rogach.scallop._
import org.elasticsearch.spark.rdd._
import howistart.{Metric, MetricBuilder}

object TcpDump {
  case class Metric(timestamp: String, ip: String, port: Int, city: String, country: String, location: Array[Float], len: Int)

  def run(sc: SparkContext, opts: Opts): Unit = {
    val geoPath = opts.geoPath().toString
    val ssc = new StreamingContext(sc, Seconds(20))
    val stream = ssc.textFileStream(opts.dumpPath().toString)
    stream.foreachRDD(lines => {
      println("Hello Streaming!")

      val metrics = lines
        .map(line => new MetricBuilder(geoPath).build(line))
        .filter(metric => metric.len > 0)

      EsSpark.saveToEs(metrics, "tcpdump/metric", Map("es.mapping.timestamp" -> "timestamp"))
    })

    ssc.start()
    ssc.awaitTermination()
  }

  class Opts(arguments: Seq[String]) extends ScallopConf(arguments) {
    val geoPath = opt[java.nio.file.Path]("geodb")
    validatePathExists(geoPath)

    val dumpPath = opt[java.nio.file.Path]("tcpdump")
    validatePathExists(dumpPath)

    verify()
  }

  def main(args: Array[String]) {
    println("Hello Spark!")

    val opts = new Opts(args)
    val conf = new SparkConf()
       .setAppName("howistart spark app")

    val sc = new SparkContext(conf)
    TcpDump.run(sc, opts)
  }

}
