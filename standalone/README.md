spark standalone
----------------

This directory contains a `docker-compose.yml` file for launching an [Apache Spark](http://spark.apache.org) standalone cluster, and a `Makefile` for deploying our Spark application to the cluster.

The `docker-compose.yml` file in this directory describes four *services*: `master`, `worker`, `elasticsearch`, and `kibana`. Run `docker-compose up` to launch all four services, then visit:

- `http://localhost:4040` to view the Spark web UI
- `http://localhost:9200` to interact with the ElasticSearch API
- `http://localhost:5601` to interact with Kibana

The [Spark application we built](../spark/README.md) has two entrypoints, one for a *batch-style* execution, and another for *streaming*. The `Makefile` in this directory contains tasks to run Docker containers that execute `spark-submit` against our standalone cluster. To run the image yourself, simply run `docker run --rm -it gettyimages/spark bin/spark-shell`. At the prompt type `:help`. To exit type `:exit`.

Run `make run-batch` to run the *batch-style* Spark application. Run `make run-streaming` to run it as a Spark *streaming* application. You are encouraged to examine the `Makefile`, as understanding it is helpful in demystifying both Docker and Spark.
