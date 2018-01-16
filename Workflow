
import org.apache.kafka.clients.producer._
import org.apache.spark.streaming._
import java.util.Properties
import org.apache.spark.streaming.kafka._
import org.apache.kafka.clients.producer.{ProducerConfig, KafkaProducer, ProducerRecord}
import play.api.libs.json._

val kafkaBrokers = "localhost:9092"
 val ssc = new StreamingContext(sc, Seconds(10))
ssc.checkpoint("checkpoint")
var zkQuorum = "localhost:2181"
var group = "1"
val topicMap = "test".split(",").map((_, 1)).toMap

 val kafkaParams = Map[String, Object](
      "bootstrap.servers" -> "localhost:2181",
      "key.deserializer" -> classOf[StringDeserializer],
      "value.deserializer" -> classOf[StringDeserializer],

      "group.id" -> "use_a_separate_group_id_for_each_stream",
      "auto.offset.reset" -> "latest",
      "enable.auto.commit" -> (false: java.lang.Boolean)
    )


val lines = KafkaUtils.createDirectStream[String, String](ssc, zkQuorum, group, topicMap).map(_._2)
lines.print()
val words = KafkaUtils.createDirectStream[String, String](
      ssc,
      PreferConsistent,
      Subscribe[String, String](Seq("tweetGroup1"), kafkaParams)
    )

val words = lines.map(_.value()).map(Json.parse).map((json: JsValue) => (json \ "text").get.toString()).flatMap(text => text.split(" ").toSeq)
val wordCounts = words.map(x => (x, 1L)).reduceByKeyAndWindow(_ + _, _ - _, Minutes(10), Seconds(10), 2)
wordCounts.print()

wordCounts.foreachRDD( rdd => {
    System.out.println("# events = " + rdd.count())
    
    rdd.foreachPartition( partition => {
      // Print statements in this section are shown in the executor's stdout logs
      val kafkaOpTopic = "test-output" 
      val props = new Properties()
      props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaBrokers)
      props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
               "org.apache.kafka.common.serialization.StringSerializer")
      props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
                "org.apache.kafka.common.serialization.StringSerializer")

      val producer = new KafkaProducer[String, String](props)
      partition.foreach( record => {
        val data = record.toString
        // As as debugging technique, users can write to DBFS to verify that records are being written out 
        // dbutils.fs.put("/tmp/test_kafka_output",data,true)
        val message = new ProducerRecord[String, String](kafkaOpTopic, null, data)      
        producer.send(message)
      } )
      producer.close()
     })
     })



ssc.start()
ssc.awaitTermination()
