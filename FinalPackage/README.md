Go to kafka folder 

cd <your path>

1.Launch Zookeeper
bin/zookeeper-server-start.sh config/zookeeper.properties 

2. launch Kafka
bin/kafka-server-start.sh config/server.properties

3. Create a topic 
bin/kafka-topics.sh --create --zookeeper localhost:2181 --replication-factor 1 --partitions 1 --topic test

4. Package scala app 
sbt package

4.launch tweet-publisher
java -jar tweet-publisher-1.0-APSHOT-jar-with-dependencies.jar -s "localhost:9092" -t "test" -f ./extract_tweets_100.json -d 2000 -l

5. Launch Spark (!!!verify spark-submit location)
/usr/lib/spark/spark-2.2.0-bin-hadoop2.7/bin/spark-submit  --packages org.apache.spark:spark-streaming-kafka-0-10_2.11:2.2.0,com.typesafe.play:play-json_2.11:2.6.0 --class "KafkaWordCount" --master local[4] target/scala-2.11/spark-kafka-project_2.11-1.0.jar localhost:2181 1 test 1

6. launch web-app
java -jar web-server-0.0.1-SNAPSHOT.jar

7. start web app in browser 
http://localhost:8080





