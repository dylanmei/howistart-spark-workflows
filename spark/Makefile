PROJECT_DIR := $(shell dirname $(PWD))
SPARK_IMAGE := "gettyimages/spark:2.0.0-hadoop-2.7"
SBT_IMAGE   := "1science/sbt:0.13.8"

default: help

help:
	# $(PROJECT_DIR)
	# make build
	# make run
	# make template

build:
	docker run -t --rm -v "$(PWD):/app" -v "$(HOME)/.ivy2":/root/.ivy2 \
		$(SBT_IMAGE) sbt assembly

run:
	docker run -t --rm --net=spark_default -v "$(PWD):/app" -v $(PROJECT_DIR)/data:/var/lib/data -v $(PROJECT_DIR)/geo:/var/lib/geo \
		$(SPARK_IMAGE) bin/spark-submit \
		--master spark://master:7077 \
		--class howistart.TcpDump \
		--deploy-mode client \
		--conf spark.es.nodes=elasticsearch:9200 \
		--conf spark.es.discovery=false \
		/app/target/scala-2.11/howistart-spark.jar \
			--geodb /var/lib/geo/GeoLiteCity.dat \
			--tcpdump /var/lib/data/tcpdump.txt \

define TEMPLATE
{
	"template": "tcpdump*",
	"mappings": {
		"_default_": {
			"properties": {
				"location": { "type": "geo_point" }
			}
		}
	}
}
endef
export TEMPLATE
template:
	@curl -si http://localhost:9200/_template/tcpdump -d "$$TEMPLATE"
