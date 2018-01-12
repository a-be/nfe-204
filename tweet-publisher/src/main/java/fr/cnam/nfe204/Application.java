package fr.cnam.nfe204;

import org.apache.kafka.clients.producer.Producer;

public class Application {
    private KafkaSender sender;
    private TweetReader tweetReader;

    Application(KafkaSender sender, TweetReader tweetReader) {
        this.sender = sender;
        this.tweetReader = tweetReader;
    }

    public static void main(String[] args) throws InterruptedException {
        Arguments arguments = Arguments.parse(args);
        Producer<Long, String> producer = KafkaProducerFactory.create(arguments.getKafkaServers());
        new Application(new KafkaSender(producer), new TweetReader()).publishTweets(arguments);
    }

    void publishTweets(Arguments arguments) throws InterruptedException {
        do {
            for (String tweet : tweetReader.readTweets(arguments.getFilePath())) {
                sender.send(arguments.getTopic(), tweet);
                Thread.sleep(arguments.getDelay());
            }
        } while (arguments.isLoop());
    }
}
