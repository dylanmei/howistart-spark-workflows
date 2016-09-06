mesos (wip)
-----------

This directory contains a `docker-compose.yml` file for launching an [Apache Mesos](http://mesos.apache.org) cluster, and a `Makefile` for deploying ElasticSearch, Kibana, and our Spark application to the cluster.

The `docker-compose.yml` file in this directory describes four *services*: `mesos`, `agent`, `marathon`, and `chronos`. Run `docker-compose up` to launch all four services, then visit:

- `http://localhost:5050` to view the Mesos master UI.
- `http://localhost:8080` to interact with Marathon.
- `http://localhost:4040` to interact with Chronos (wip).

In the previous exercises, we used Docker Compose to launch ElasticSearch and Kibana. This time we'll use [Marathon](https://mesosphere.github.io/marathon/), Mesos' orchestration platform, to launch these services for us:

- Run `make run-elasticsearch` to launch ElasticSearch
- Run `make run-kibana` to launch Kibana

Now visit:

- `http://localhost:5050` to interact with the ElasticSearch API
- `http://localhost:8080` to interact with Kibana

(wip) Run `make run-batch` to run the *batch-style* Spark application using [Chronos](https://mesos.github.io/chronos), a distributed *cron-like* scheduler. It will run our Spark application once immediately and every fifteen minutes thereafter.

Run `make run-streaming` to run the Spark *streaming* application using Marathon, a distributed *init-like* scheduler. It will run our Spark application indefinately. It will show up as a Mesos framework in the Frameworks tab.

Similar to our Zeppelin exercise, we need to populate the `data` directory with live data. From the project directory, run `tcpdump` and `split` together to break our logs into files, 200 lines each: `sudo tcpdump -qfn -tttt -i wlp1s0 -Q out '(tcp[tcpflags] & tcp-push != 0)' | split -l 200 - data/tcpdump-`. Your `-i` interface argument may differ; run `tcpdump --list-interfaces` to determine the interface name of your internet connection.

**NOTE** Because we've used Mesos to launch the ElasticSearch and Kibana Docker containers, simply exiting Docker Compose isn't enough to shut down these services. Exit them in Marathon prior to exiting Docker Compose, or remove them manually using the `docker rm` command.
