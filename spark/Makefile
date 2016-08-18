default: help

help:
	# make build
	# make deploy

build:
	docker run --rm \
		-v "$(PWD):/app" \
		-v "$(HOME)/.ivy2":/root/.ivy2 \
		1science/sbt:0.13.8 sbt assembly

deploy:
	docker run --rm --net=spark_default -v "$(PWD):/app" \
	gettyimages/spark:2.0.0-hadoop-2.7 \
		bin/spark-submit \
		--master spark://master:7077 \
		--class howistart.TcpDump \
		--deploy-mode client \
		/app/target/scala-2.11/howistart.jar
