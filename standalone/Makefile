PROJECT_DIR := $(shell dirname $(PWD))
SPARK_IMAGE := "gettyimages/spark:2.0.0-hadoop-2.7"

default: help

help:
	# $(PROJECT_DIR)
	# make run-batch
	# make run-streaming
	# make setup-elasticsearch-index-template
	# make remove-elasticsearch-index
	# 
	# Spark is running at localhost:4040
	# ElasticSearch is running at localhost:9200
	# Kibana is running at localhost:5601
	#
	# tcpdump to file:       sudo tcpdump -qfn -tttt -i wlp1s0 'src net 10.0.0.0/8 and (tcp[tcpflags] & tcp-push != 0)' | tee $(PROJECT_DIR)/data/tcpdump.txt
	# tcpdump to directory:  sudo tcpdump -qfn -tttt -i wlp1s0 'src net 10.0.0.0/8 and (tcp[tcpflags] & tcp-push != 0)' | split -l 100 - $(PROJECT_DIR)/data/tcpdump-
	# tcpdump ls interfaces: sudo tcpdump --list-interfaces

run-batch: setup-elasticsearch-index-template
	docker run -t --rm --net=standalone_spark -p 9999:9999 -v "$(PROJECT_DIR)/spark:/app" -v $(PROJECT_DIR)/data:/var/lib/data -v $(PROJECT_DIR)/geo:/var/lib/geo \
		$(SPARK_IMAGE) bin/spark-submit \
		--master spark://master:7077 \
		--class howistart.batch.TcpDump \
		--deploy-mode client \
		--conf spark.es.nodes=elasticsearch:9200 \
		--conf spark.es.discovery=false \
		/app/target/scala-2.11/howistart-spark.jar \
			--geodb /var/lib/geo/GeoLiteCity.dat \
			--tcpdump /var/lib/data/tcpdump.txt

run-streaming: setup-elasticsearch-index-template
	docker run -t --rm --net=standalone_spark -p 9999:9999 -v "$(PROJECT_DIR)/spark:/app" -v $(PROJECT_DIR)/data:/var/lib/data -v $(PROJECT_DIR)/geo:/var/lib/geo \
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
