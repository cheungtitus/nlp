package com.kenrui.nlp.nlpcore;

import com.kenrui.nlp.nlpcore.models.Comment;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

@Component("NLPControllerMVC")
@RestController
public class NLPControllerMVC {
    private static final Logger logger = LogManager.getLogger("NLP");

    Pipeline pipeline = new Pipeline();

    @PostMapping("/getsentiment")
    public String getSentiment(@RequestBody Comment comment) {
        return pipeline.getSentiment(comment.getBody());
    }

    @GetMapping ("/getsentiment/{sentence}")
    public String getSentiment(@PathVariable(value = "sentence") String sentence) {
        return pipeline.getSentiment(sentence);
    }

    @GetMapping ("/check")
    public String check() {
        return "NLPControllerMVC";
    }

}
