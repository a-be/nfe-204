package fr.cnam.nfe204.webserver;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class Occurences {
    Map<String,Integer> occurences = new HashMap<>();

    public List<Occurence> get() {
        return occurences.entrySet().stream().map(entry -> new Occurence(entry.getKey(), entry.getValue())).collect(Collectors.toList());
    }

    public void set(String word, int counter) {
        this.occurences.put(word, counter);
    }
}
