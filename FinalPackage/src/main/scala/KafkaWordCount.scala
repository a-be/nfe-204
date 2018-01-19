import java.util.HashMap

import org.apache.kafka.clients.producer._
import org.apache.spark.streaming._
import org.apache.spark.SparkConf
import java.util.Properties
import org.apache.kafka.clients.producer.{ProducerConfig, KafkaProducer, ProducerRecord}
import org.apache.spark.streaming.kafka010.ConsumerStrategies.Subscribe
import org.apache.spark.streaming.kafka010.LocationStrategies.PreferConsistent
import org.apache.log4j.Logger
import org.apache.log4j.Level
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.spark.streaming.dstream.DStream
import org.apache.spark.streaming.kafka010._
import org.apache.spark.streaming.{Seconds, StreamingContext}
import play.api.libs.json._

object KafkaWordCount {
   def main(args: Array[String]) {
      if (args.length < 4) {
         System.err.println("Usage: KafkaWordCount <zkQuorum> <group> <topics> <numThreads>")
         System.exit(1)
      }

      Logger.getLogger("org").setLevel(Level.OFF)
      Logger.getLogger("akka").setLevel(Level.OFF)
      val Array(zkQuorum, group, topics, numThreads) = args
      val sparkConf = new SparkConf().setAppName("KafkaWordCount")
      val ssc = new StreamingContext(sparkConf, Seconds(2))
      ssc.checkpoint("checkpoint")

      val kafkaBrokers = "localhost:9092"
      val kafkaParams = Map[String, Object](
      "bootstrap.servers" -> kafkaBrokers,
      "key.deserializer" -> classOf[StringDeserializer],
      "value.deserializer" -> classOf[StringDeserializer],

      "group.id" -> "use_a_separate_group_id_for_each_stream",
      "auto.offset.reset" -> "latest",
      "enable.auto.commit" -> (false: java.lang.Boolean)
      )
      
      val stream = KafkaUtils.createDirectStream[String, String](
      ssc,
      PreferConsistent,
      Subscribe[String, String](Seq("test"), kafkaParams)
    )
       val words = stream.map(_.value()).map(Json.parse).map((json: JsValue) => (json \ "text").get.toString()).flatMap(text => text.split(" ").toSeq)

      val wordCounts = words.map(x => (x, 1L)).reduceByKeyAndWindow(_ + _, _ - _, Minutes(10), Seconds(10), 2)
      wordCounts.print()

    wordCounts.foreachRDD( rdd => {
        System.out.println("# events = " + rdd.count())
        
        rdd.foreachPartition( partition => {
          // Print statements in this section are shown in the executor's stdout logs
          val kafkaOpTopic = "nfe-topic" 
          val props = new Properties()
          props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaBrokers)
          props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
                   "org.apache.kafka.common.serialization.StringSerializer")
          props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
                    "org.apache.kafka.common.serialization.StringSerializer")

          val producer = new KafkaProducer[String, String](props)

          // Print statements in this section are shown in the executor's stdout logs
         
          partition.foreach( record => {
            val data = record.toString
            // As as debugging technique, users can write to DBFS to verify that records are being written out 
            // dbutils.fs.put("/tmp/test_kafka_output",data,true)
            val message = new ProducerRecord[String, String](kafkaOpTopic, null, data)      
            producer.send(message)
          } )
            //val jsonMessage = new StringBuilder
            //jsonMessage.append("[")
          //partition.foreach( record => {
           // val dataArray = record.toString.substring(1, record.toString.size-1).split(",")
            //val data = "{\"word\":\"" + dataArray(0).toString + "\", \"occurence\":\"" + dataArray(1).toString + "\"},"
            //jsonMessage.append(data)
            // As as debugging technique, users can write to DBFS to verify that records are being written out 
            // dbutils.fs.put("/tmp/test_kafka_output",data,true)
         // } )
            //val jsonMessageToSend = jsonMessage.dropRight(1) + "]"
            //scala.tools.nsc.io.File("/home/uness/Cours/hackathon/Hack/nfe-204/visualisation/test.json").writeAll(jsonMessageToSend.toString)
           // val message = new ProducerRecord[String, String](kafkaOpTopic, null, jsonMessageToSend.toString)      
            //producer.send(message)
          producer.close()
     })
})

      ssc.start()
      ssc.awaitTermination()
   }
}
