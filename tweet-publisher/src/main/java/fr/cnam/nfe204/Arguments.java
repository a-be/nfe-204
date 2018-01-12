package fr.cnam.nfe204;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.OutputStream;
import java.nio.file.Path;
import java.util.List;

import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;
import org.kohsuke.args4j.spi.BooleanOptionHandler;
import org.kohsuke.args4j.spi.StringArrayOptionHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class Arguments {
    private static final Logger LOGGER = LoggerFactory.getLogger(Arguments.class);

    @Option(name="-f", required = true, usage="File path to read")
    private File filePath;
    @Option(name="-t", required = true, usage="Kafka topic")
    private String topic;
    @Option(name="-d", usage="Delay between sends")
    private long delay = 1000;
    @Option(name="-l", handler=BooleanOptionHandler.class, usage="Loop indefinetly over file")
    private boolean loop = false;
    @Option(name="-s", handler = StringArrayOptionHandler.class, required = true, usage="kafka servers space separated")
    private List<String> kafkaServers;

    Path getFilePath() {
        return filePath.toPath();
    }

    List<String> getKafkaServers() {
        return kafkaServers;
    }

    String getTopic() {
        return topic;
    }

    long getDelay() {
        return delay;
    }

    boolean isLoop() {
        return loop;
    }

    static Arguments parse(String[] args){
        Arguments bean = new Arguments();
        CmdLineParser parser = new CmdLineParser(bean);
        try {
            parser.parseArgument(args);
            return bean;
        } catch (CmdLineException e) {
            OutputStream stream = new ByteArrayOutputStream();
            parser.printUsage(stream);
            LOGGER.warn(stream.toString(), e);
            throw new RuntimeException(e);
        }
    }
}
