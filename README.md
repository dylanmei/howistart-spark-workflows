howistart-spark-workflows
-------------------------

Working with distributed systems can be challenging. Ideally, the creative process of authoring solutions is not blocked due to unavailable infrastructure. With Docker and Docker Compose we can get going with little fuss, iterating on our ideas well before going to production.

The project is part of a talk that demonstrates sketching out an idea in Zeppelin, building out that idea in Spark, and executing it on both a local Spark standalone cluster and a Mesos cluster. The repo contains four key directories:

- [./zeppelin](zeppelin/README.md) contains a `docker-compose.yml` file that launches an [Apache Zeppelin](http://zeppelin.apache.org) notebook. We'll use this notebook to sketch out an initial idea.
- [./spark](spark/README.md) contains a Scala project for our [Apache Spark](http://spark.apache.org) application.
- [./standalone](standalone/README.md) contains a `docker-compose.yml` file that launches a Spark standalone cluster. We can run our Spark application on this cluster.
- [./mesos](mesos/README.md) contains a `docker-compose.yml` file that launches a [Apache Mesos](http://mesos.apache.org) cluster. We can run our Spark applicaton on this cluster.
