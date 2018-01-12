package fr.cnam.nfe204;

import java.nio.file.Paths;

import org.assertj.core.api.Assertions;
import org.junit.Test;

public class ArgumentsTest {

    @Test(expected = RuntimeException.class)
    public void parseWithNoFilePathShouldThrowException() {
        String[] args = { "-s", "server1 server2" };
        Arguments.parse(args);
    }

    @Test(expected = RuntimeException.class)
    public void parseWithNoServersShouldThrowException() {
        String[] args = {"-f", "/test/tweets", "-t", "topic-test"};
        Arguments.parse(args);
    }

    @Test(expected = RuntimeException.class)
    public void parseWithNoTopicShouldThrowException() {
        String[] args = {"-s", "server1 server2", "-t", "topic-test"};
        Arguments.parse(args);
    }

    @Test
    public void parseWithValidParametersShouldReturnArguments() {
        String[] args = {"-f", "/test/tweets", "-t", "topic-test", "-s", "server1 server2" };

        Arguments arguments = Arguments.parse(args);

        Assertions.assertThat(arguments.getFilePath()).isEqualTo(Paths.get("/test/tweets"));
        Assertions.assertThat(arguments.getTopic()).isEqualTo("topic-test");
        Assertions.assertThat(arguments.getKafkaServers()).contains("server1", "server2");
        Assertions.assertThat(arguments.getDelay()).isEqualTo(1000);
        Assertions.assertThat(arguments.isLoop()).isFalse();
    }

    @Test
    public void parseWithDelayShouldReturnArgumentsWithExpectedDelay() {
        String[] args = {"-f", "/test/tweets", "-t", "topic-test", "-s", "server1 server2", "-d", "2000" };

        Arguments arguments = Arguments.parse(args);

        Assertions.assertThat(arguments.getDelay()).isEqualTo(2000);
    }

    @Test
    public void parseWithNoLoopingShouldReturnArgumentsWithLooping() {
        String[] args = {"-f", "/test/tweets", "-t", "topic-test", "-s", "server1 server2", "-l" };

        Arguments arguments = Arguments.parse(args);

        Assertions.assertThat(arguments.isLoop()).isTrue();
    }
}
