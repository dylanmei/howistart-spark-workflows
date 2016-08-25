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
      println("Hello Streaming!")

      val metrics = lines
        .map(_.split(" "))
        .filter(_.size == 8)
        .map(attrs => {
          val timestamp = new DateTime(attrs(0) + "T" + attrs(1), DateTimeZone.forID("America/Los_Angeles"))
          val remote = attrs(5)
          val (ip, port) = remote.splitAt(remote.lastIndexOf("."))

          val ipl = IpLookups(geoFile = Option(geoPath))

          val (city, country, lat:Float, lon:Float) = ipl.performLookups(ip)._1 match {
            case Some(geo) => (geo.city, Option(geo.countryName), geo.latitude, geo.longitude)
            case _ => (None, None, 0, 0)
          }

          Metric(timestamp.toString(ISODateTimeFormat.dateTime()),
            ip,
            port.stripPrefix(".").stripSuffix(":").toInt,
            city.getOrElse(""),
            country.getOrElse(""),
            Array(lon, lat),
            attrs(7).toInt)
         })

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
