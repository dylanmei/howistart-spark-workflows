PROJECT_DIR := $(shell dirname $(PWD))

default: help

help:
	# $(PROJECT_DIR)
	# make setup-elasticsearch-index-template
	# make remove-elasticsearch-index
	# 
	# Mesos is running at localhost:5050
	# Marathon is running at localhost:8080
	# ElasticSearch is running at localhost:9200
	# Kibana is running at localhost:5601
	#
	# tcpdump to file:       sudo tcpdump -qfn -tttt -i wlp1s0 'src net 10.0.0.0/8 and (tcp[tcpflags] & tcp-push != 0)' | tee $(PROJECT_DIR)/data/tcpdump.txt
	# tcpdump to directory:  sudo tcpdump -qfn -tttt -i wlp1s0 'src net 10.0.0.0/8 and (tcp[tcpflags] & tcp-push != 0)' | split -l 100 - $(PROJECT_DIR)/data/tcpdump-
	# tcpdump ls interfaces: sudo tcpdump --list-interfaces

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
