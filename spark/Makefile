PROJECT_DIR := $(shell dirname $(PWD))
SBT_IMAGE   := "1science/sbt:0.13.8"

default: help

help:
	# $(PROJECT_DIR)
	# make build

build:
	docker run -t --rm -v "$(PWD):/app" -v "$(HOME)/.ivy2":/root/.ivy2 \
		$(SBT_IMAGE) sbt assembly
