spark app
---------

This directory contains a `Makefile` and a Scala project for our [Apache Spark](http://spark.apache.org) application.

The `Makefile` contains a task to build the project using an Docker image with Scala and [sbt](http://www.scala-sbt.org/) installed. To run the image yourself, simply run: `docker run --rm -it -v ~/.ivy2:/root/.ivy2 1science/sbt:0.13.8 sbt`. At the prompt type `help`. To exit type `exit`.

To build the Spark application, run `make build`. When complete, there will be a Java jar at `./target/scala-2.11/howistart-spark.jar`. We will use this jar in future exercises.
