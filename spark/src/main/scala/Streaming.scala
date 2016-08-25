package howistart.streaming

import org.apache.spark._
import org.apache.spark.streaming._
import org.rogach.scallop._
import org.joda.time.{DateTime, DateTimeZone}
import org.joda.time.format._
import com.snowplowanalytics.maxmind.iplookups.IpLookups
import org.elasticsearch.spark.rdd._

object TcpDump {
  case class Metric(timestamp: String, ip: String, port: Int, city: String, country: String, location: Array[Float], len: Int)

  def run(sc: SparkContext, opts: Opts): Unit = {
    val geoPath = opts.geoPath().toString
    val ssc = new StreamingContext(sc, Seconds(20))
    val stream = ssc.textFileStream(opts.dumpPath().toString)
    stream.foreachRDD(lines => {
      lines.foreach(println)
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
