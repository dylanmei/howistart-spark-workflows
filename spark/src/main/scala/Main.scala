package howistart

import org.apache.spark._
import org.joda.time._
import org.joda.time.format._
import com.snowplowanalytics.maxmind.iplookups.IpLookups
import org.elasticsearch.spark.rdd._

object TcpDump {
  case class Metric(timestamp: String, ip: String, port: Int, city: String, country: String, location: Array[Float], len: Int)

  def main(args: Array[String]) {
    println("Hello Spark!")
    val opts = new Opts(args)

    val conf = new SparkConf().setAppName("TcpDump Batch")
    val sc = new SparkContext(conf)

    TcpDump.run(sc, opts)
    sc.stop()
  }

  def run(sc: SparkContext, opts: Opts): Unit = {
    var geoPath = opts.geoPath().toString
    val lines = sc.textFile(opts.dumpPath().toString)
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
  }
}
