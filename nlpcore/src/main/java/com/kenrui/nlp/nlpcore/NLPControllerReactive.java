package com.kenrui.nlp.nlpcore;

import com.kenrui.nlp.common.entities.Comment;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@Component("NLPControllerReactive")
@RestController
public class NLPControllerReactive {
    private static final Logger logger = LogManager.getLogger("NLP");

    Pipeline pipeline = new Pipeline();

    @PostMapping("/getsentiment")
    public Mono<String> getSentiment(@RequestBody Comment comment) {
        return Mono.just(pipeline.getSentiment(comment.getText()));
    }

    @GetMapping ("/getsentiment/{sentence}")
    public Mono<String> getSentiment(@PathVariable(value = "sentence") String sentence) {
        return Mono.just(pipeline.getSentiment(sentence));
    }

    @GetMapping ("/check")
    public String check() {
        return "NLPControllerReactive";
    }
}
