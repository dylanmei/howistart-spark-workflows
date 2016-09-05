zeppelin
--------

[Apache Zeppelin](http://zeppelin.apache.org) is a web-based notebook that integrates well with data-processing backends such as Apache Spark, JDBC, ElasticSearch, and others.

The `docker-compose.yml` file in this directory describes three *services*: `zeppelin`, `elasticsearch`, and `kibana`. Run `docker-compose up` to launch all three services, then visit `http://localhost:8080` to interact with Zeppelin.
