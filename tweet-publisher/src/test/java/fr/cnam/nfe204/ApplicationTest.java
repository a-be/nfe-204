package fr.cnam.nfe204;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ApplicationTest {
    private Application application;
    @Mock
    private KafkaSender sender;
    @Mock
    private TweetReader tweetReader;
    @Mock
    private Arguments arguments;


    @Before
    public void setUp() {
        application = new Application(sender, tweetReader);
    }

    @Test
    public void publishTweets() throws InterruptedException {
        application.publishTweets(arguments);
    }

}
