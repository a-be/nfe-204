package fr.cnam.nfe204;

import java.util.concurrent.ExecutionException;

import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class KafkaSender {
    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaSender.class);

    private Producer<Long, String> producer;

    KafkaSender(Producer<Long, String> producer) {
        this.producer = producer;
    }

    void send(String topic, String message) {
        long time = System.currentTimeMillis();
        final ProducerRecord<Long, String> record = new ProducerRecord<>(topic, time, message);
        try {
            RecordMetadata metadata = producer.send(record).get();
            LOGGER.info("sent record(key={} value={}) meta(partition={}, offset={})", record.key(), record.value(), metadata.partition(),
                    metadata.offset());
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        } finally {
            producer.flush();
        }
    }
}
