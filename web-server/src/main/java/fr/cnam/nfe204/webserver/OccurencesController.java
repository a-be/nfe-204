package fr.cnam.nfe204.webserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class OccurencesController {
    private static final Logger LOGGER = LoggerFactory.getLogger(OccurencesReceiver.class);

    @Autowired
    private Occurences occurences;

    @RequestMapping("/occurence")
    public List<Occurence> getOccurence() {
        List<Occurence> occurences = this.occurences.get();
        LOGGER.info("send : {}", occurences);
        return occurences;
    }
}
