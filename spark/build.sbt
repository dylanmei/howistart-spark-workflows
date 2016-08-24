name := "howistart-spark"

organization := "howistart"

assemblyJarName in assembly := "howistart-spark.jar"

/* scala versions and options */
scalaVersion := "2.11.8"

// These options will be used for *all* versions.
scalacOptions ++= Seq(
  "-deprecation"
  ,"-unchecked"
  ,"-encoding", "UTF-8"
  ,"-Xlint"
  ,"-Yclosure-elim"
  ,"-Yinline"
  ,"-Xverify"
  ,"-feature"
  ,"-language:postfixOps"
  //,"-optimise"
)

javacOptions ++= Seq("-Xlint:unchecked", "-Xlint:deprecation")

/* dependencies */
resolvers += "SnowPlow Repo" at "http://maven.snplow.com/releases/"

libraryDependencies ++= Seq (
  "org.apache.spark" %% "spark-core" % "2.0.0" % "provided",
  "org.apache.spark" %% "spark-sql" % "2.0.0" % "provided",
  "org.apache.spark" %% "spark-streaming" % "2.0.0" % "provided",
  "joda-time" % "joda-time" % "2.9.4" % "provided",
  "org.rogach" %% "scallop" % "2.0.1",
  "com.snowplowanalytics"  %% "scala-maxmind-iplookups"  % "0.2.0",
  "org.elasticsearch" %% "elasticsearch-spark-20" % "5.0.0-alpha5"
)

fork := true

packageArchetype.java_server

mergeStrategy in assembly := {
  case m if m.toLowerCase.endsWith("manifest.mf")          => MergeStrategy.discard
  case m if m.toLowerCase.matches("meta-inf.*\\.sf$")      => MergeStrategy.discard
  case "log4j.properties"                                  => MergeStrategy.discard
  case m if m.toLowerCase.startsWith("meta-inf/services/") => MergeStrategy.filterDistinctLines
  case "reference.conf"                                    => MergeStrategy.concat
  case _                                                   => MergeStrategy.first
}
