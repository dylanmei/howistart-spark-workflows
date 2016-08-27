PROJECT_DIR := $(shell dirname $(PWD))
SPARK_IMAGE := "gettyimages/spark:2.0.0-hadoop-2.7"
SBT_IMAGE   := "1science/sbt:0.13.8"

default: help

help:
	# $(PROJECT_DIR)
	# make build
	# make run-batch-in-standalone-cluster
	# make run-streaming-in-standalone-cluster
	# make setup-elasticsearch-index-template
	# make remove-elasticsearch-index

build:
	docker run -t --rm -v "$(PWD):/app" -v "$(HOME)/.ivy2":/root/.ivy2 \
		$(SBT_IMAGE) sbt assembly

run-batch-in-standalone-cluster: build setup-elasticsearch-index-template
	docker run -t --rm --net=standalone_spark -p 9999:9999 -v "$(PWD):/app" -v $(PROJECT_DIR)/data:/var/lib/data -v $(PROJECT_DIR)/geo:/var/lib/geo \
		$(SPARK_IMAGE) bin/spark-submit \
		--master spark://master:7077 \
		--class howistart.batch.TcpDump \
		--deploy-mode client \
		--conf spark.es.nodes=elasticsearch:9200 \
		--conf spark.es.discovery=false \
		/app/target/scala-2.11/howistart-spark.jar \
			--geodb /var/lib/geo/GeoLiteCity.dat \
			--tcpdump /var/lib/data/tcpdump.txt

run-streaming-in-standalone-cluster: build setup-elasticsearch-index-template
	docker run -t --rm --net=standalone_spark -p 9999:9999 -v "$(PWD):/app" -v $(PROJECT_DIR)/data:/var/lib/data -v $(PROJECT_DIR)/geo:/var/lib/geo \
		$(SPARK_IMAGE) bin/spark-submit \
		--master spark://master:7077 \
		--class howistart.streaming.TcpDump \
		--deploy-mode client \
		--supervise \
		--conf spark.es.nodes=elasticsearch:9200 \
		--conf spark.es.discovery=false \
		/app/target/scala-2.11/howistart-spark.jar \
			--geodb /var/lib/geo/GeoLiteCity.dat \
			--tcpdump /var/lib/data

remove-elasticsearch-index:
	@curl -si -XDELETE http://localhost:9200/tcpdump

define TEMPLATE
{
	"template": "tcpdump*",
	"mappings": {
		"_default_": {
			"properties": {
				"location": { "type": "geo_point" },
				"city": { "type": "string", "index": "not_analyzed" },
				"country": { "type": "string", "index": "not_analyzed" }
			}
		}
	}
}
endef
export TEMPLATE
setup-elasticsearch-index-template:
	@curl -si http://localhost:9200/_template/tcpdump -d "$$TEMPLATE"
