package fr.cnam.nfe204.webserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;

import java.util.List;

public class OccurencesReceiver {
    private static final Logger LOGGER = LoggerFactory.getLogger(OccurencesReceiver.class);

    @Autowired
    private Occurences occurences;

    @KafkaListener(topics = "${kafka.topic}")
    public void receive(String occurence) {
        String[] data = occurence.replace("(", "").replace(")", "").replace(" ", "").split(",");
        String word = data[0];
        String counter = data[1];
        LOGGER.info("received : {}", occurences);
        this.occurences.set(word, Integer.parseInt(counter));
    }
}