package fr.cnam.nfe204;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

class TweetReader {
    List<String> readTweets(Path filePath){
        try {
            return Files.readAllLines(filePath);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
