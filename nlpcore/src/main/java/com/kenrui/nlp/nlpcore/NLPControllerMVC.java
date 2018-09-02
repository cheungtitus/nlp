package com.kenrui.nlp.nlpcore;

import com.kenrui.nlp.common.entities.Comment;
import com.kenrui.nlp.common.entities.Sentiment;
import com.kenrui.nlp.common.entities.Taxonomy;
import com.kenrui.nlp.common.repositories.CommentRepository;
import com.kenrui.nlp.common.repositories.SentimentRepository;
import com.kenrui.nlp.common.repositories.TaxonomyRepository;
import com.kenrui.nlp.common.utilities.JsonHelper;
import com.kenrui.nlp.taxonomy.TaxonomyModelWrapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.util.List;


// Really should move this class to the com.kenrui.nlp.common package
@Component("NLPControllerMVC")
@RestController
public class NLPControllerMVC {
    private static final Logger logger = LogManager.getLogger("NLP");

    @Autowired private CommentRepository commentRepository;
    @Autowired private SentimentRepository sentimentRepository;
    @Autowired private TaxonomyRepository taxonomyRepository;

    private Pipeline pipeline = new Pipeline();
    TaxonomyModelWrapper taxonomyModelWrapper = new TaxonomyModelWrapper();

    @GetMapping("/gettaxonomy")
    public String getTaxonomy() {
        return JsonHelper.write(taxonomyRepository.findAll());
    }

    @PostMapping("/getsentiment")
    public String getSentiment(@RequestBody Comment comment) {
        return pipeline.getSentiment(commentRepository.save(comment).getBody());
    }

    @GetMapping ("/getsentiment")
    public String getSentiment(@RequestParam String body,
                               @RequestParam String postedBy) {

        Comment comment = new Comment(body, postedBy);

        String sentiment = pipeline.getSentiment(comment.getBody());
        comment.addSentiment(new Sentiment(sentiment));

        List<Taxonomy> taxonomyList = taxonomyModelWrapper.getTaxonomyFromModels(comment.getBody());
        comment.addTaxonomies(taxonomyList);

        commentRepository.save(comment);

        logger.trace("Comment Repository");
        commentRepository.findAll().forEach(item -> {
            logger.trace(JsonHelper.write(item));
        });

        logger.trace("Sentiment Repository");
        sentimentRepository.findAll().forEach(item -> {
            logger.trace(JsonHelper.write(item));
        });

        logger.trace("Taxonomy Repository");
        taxonomyRepository.findAll().forEach(item -> {
            logger.trace(JsonHelper.write(item));
        });

        return sentiment;
    }

    @GetMapping ("/check")
    public String check() {
        return "NLPControllerMVC";
    }

}
