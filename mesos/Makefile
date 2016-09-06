PROJECT_DIR := $(shell dirname $(PWD))

default: help

help:
	# $(PROJECT_DIR)
	# make run-streaming
	# make run-elasticsearch
	# make run-kibana
	# make setup-elasticsearch-index-template
	# make remove-elasticsearch-index
	# 
	# Mesos is running at localhost:5050
	# Marathon is running at localhost:8080
	# Chronos is running at localhost:4040

run-streaming:
	@curl -si -XPUT -H "Content-Type: application/json" http://localhost:8080/v2/apps/spark-streaming -d@spark-streaming.json

run-elasticsearch:
	@curl -si -XPUT -H "Content-Type: application/json" http://localhost:8080/v2/apps/elasticsearch -d@elasticsearch.json

run-kibana:
	@curl -si -XPUT -H "Content-Type: application/json" http://localhost:8080/v2/apps/kibana -d@kibana.json

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
