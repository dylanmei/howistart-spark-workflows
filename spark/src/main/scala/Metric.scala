package howistart

case class Metric(timestamp: String, ip: String, port: Int, len: Int, city: String = "", country: String = "", location: Array[Float] = Array(0, 0))

class MetricBuilder(val geodb: String) {
    import org.joda.time.format._
    import org.joda.time.{DateTime, DateTimeZone}
    import com.snowplowanalytics.maxmind.iplookups.IpLookups

    def build(line: String): Metric = {
        val attrs = line.split(" ")
        if (attrs.size < 8) {
            return new Metric("", "", 0, 0)
        }

        val datetime = new DateTime(attrs(0) + "T" + attrs(1), DateTimeZone.forID("America/Los_Angeles"))
        val timestamp = datetime.toString(ISODateTimeFormat.dateTime())

        val remote = attrs(5)
        val (ip, port) = remote.splitAt(remote.lastIndexOf("."))

        val ipl = IpLookups(geoFile = Some(geodb))
        val (city, country, lat:Float, lon:Float) = ipl.performLookups(ip)._1 match {
            case Some(geo) => (geo.city, Option(geo.countryName), geo.latitude, geo.longitude)
            case _ => (None, None, 0, 0)
        }

        return Metric(timestamp, ip, port.stripPrefix(".").stripSuffix(":").toInt,
            attrs(7).toInt, city.getOrElse(""), country.getOrElse(""), Array(lon, lat))
    }
}
