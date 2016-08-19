package howistart

import org.rogach.scallop._

class Opts(arguments: Seq[String]) extends ScallopConf(arguments) {
  val geoPath = opt[java.nio.file.Path]("geodb")
  validatePathExists(geoPath)
  val dumpPath = opt[java.nio.file.Path]("tcpdump")
  validatePathExists(dumpPath)
  verify()
}


