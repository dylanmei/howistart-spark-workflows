zeppelin
--------

[Apache Zeppelin](http://zeppelin.apache.org) is a web-based notebook that integrates well with data-processing backends such as Apache Spark, JDBC, ElasticSearch, and others.

The `docker-compose.yml` file in this directory describes three *services*: `zeppelin`, `elasticsearch`, and `kibana`. Run `docker-compose up` to launch all three services, then visit:

- `http://localhost:4040` to interact with Zeppelin
- `http://localhost:9200` to interact with the ElasticSearch API
- `http://localhost:5601` to interact with Kibana

The exercises are available as Zeppelin notebooks:

- 1. **TcpDump with Spark SQL**
- 2. **TcpDump with ElasticSearch**

There are also setup guides to help with installing dependencies and using `tcpdump`.

Once a Spark context is available (by running a `%spark`, `%pyspark`, or `%sql` [Zeppelin interpreter](http://zeppelin.apache.org/docs/0.7.0-SNAPSHOT/manual/interpreters.html), you can visit `http://localhost:4040` to view the Spark web UI.
