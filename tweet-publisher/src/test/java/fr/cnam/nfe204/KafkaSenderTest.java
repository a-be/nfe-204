package fr.cnam.nfe204;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.TopicPartition;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class KafkaSenderTest {
    private KafkaSender kafkaSender;
    @Mock
    Producer<Long, String> producer;
    @Mock
    Future<RecordMetadata> future;

    @Before
    public void setUp() {
        kafkaSender = new KafkaSender(producer);
    }

    @Test
    public void sendWithTopicAndMessageShouldUseProducer() throws ExecutionException, InterruptedException {
        Mockito.when(producer.send(Mockito.any())).thenReturn(future);

        RecordMetadata metadata = new RecordMetadata(new TopicPartition("test-topic", 3), 0, 0, 0, null, 0, 0);
        Mockito.when(future.get()).thenReturn(metadata);

        kafkaSender.send("test-topic", "this is the content");

        Mockito.verify(producer).send(Mockito.any(ProducerRecord.class));
    }

}
