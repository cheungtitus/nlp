package com.kenrui.nlp.nlpcore;

import com.kenrui.nlp.common.entities.*;
import com.kenrui.nlp.common.repositories.CommentRepository;
import com.kenrui.nlp.common.repositories.SentimentRepository;
import com.kenrui.nlp.common.repositories.TaxonomyCategoryRepository;
import com.kenrui.nlp.common.repositories.TaxonomyProductRepository;
import com.kenrui.nlp.common.utilities.JsonHelper;
import com.kenrui.nlp.models.ItemSubmitted;
import com.kenrui.nlp.taxonomy.TaxonomyModelWrapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


// Really should move this class to the com.kenrui.nlp.common package
@Component("NLPControllerMVC")
@CrossOrigin
@RestController
public class NLPControllerMVC {
    private static final Logger logger = LogManager.getLogger("NLP");

    @Autowired private CommentRepository commentRepository;
    @Autowired private SentimentRepository sentimentRepository;
    @Autowired private TaxonomyCategoryRepository taxonomyCategoryRepository;
    @Autowired private TaxonomyProductRepository taxonomyProductRepository;

    private Pipeline pipeline = new Pipeline();
    TaxonomyModelWrapper taxonomyModelWrapper = new TaxonomyModelWrapper();

    @PostMapping("/postComment")
    public ResponseEntity postComment(@RequestBody Comment comment) {
        return new ResponseEntity(JsonHelper.write(comment), HttpStatus.OK);
    }

    @PostMapping("/postItem")
    public ResponseEntity postComment(@RequestBody ItemSubmitted itemSubmitted) {
        Comment comment = new Comment(itemSubmitted.getText(), itemSubmitted.getPostedBy());
        String sentiment = pipeline.getSentiment(commentRepository.save(comment).getText());
        String taxonomyCategory, taxonomyProduct;

        if (itemSubmitted.getComplaint()) {
            // If it is a complaint we need operator to specify taxonomy details as well so
            // we can tell how accurate the AI models are doing over time
            if (itemSubmitted.getDetails().isEmpty()) {
                return new ResponseEntity(JsonHelper.write(itemSubmitted), HttpStatus.NOT_ACCEPTABLE);
            } else {
                itemSubmitted.getDetails().forEach((taxonomyItem) -> {
                    switch(taxonomyItem.getId().intValue()) {
                        case 0:
                            Optional<TaxonomyCategory> taxonomyCategoryOptional =
                                    taxonomyCategoryRepository.findById(taxonomyItem.getSelectedItemId());
                            if (taxonomyCategoryOptional.isPresent()) {
                                taxonomyCategoryOptional.get().getCategory();
                            }
                            break;
                        case 1:
                            Optional<TaxonomyProduct> taxonomyProductOptional =
                                    taxonomyProductRepository.findById(taxonomyItem.getSelectedItemId());
                            if (taxonomyProductOptional.isPresent()) {
                                taxonomyProductOptional.get().getProduct();
                            }
                            break;
                    }
                });
            }
        } else {
            // If it is not a complaint we consider this as a feedback and only do sentiment analysis
            return new ResponseEntity(sentiment, HttpStatus.OK);
        }
        return new ResponseEntity(JsonHelper.write(itemSubmitted), HttpStatus.OK);
    }

    @GetMapping("/gettaxonomycategory")
    public String getTaxonomyCategory() {
        return JsonHelper.write(taxonomyCategoryRepository.findAll());
    }

    @PostMapping("/getsentiment")
    public String getSentiment(@RequestBody Comment comment) {
        return pipeline.getSentiment(commentRepository.save(comment).getText());
    }

    @GetMapping ("/getsentiment")
    public String getSentiment(@RequestParam String body,
                               @RequestParam String postedBy) {

        Comment comment = new Comment(body, postedBy);

        String sentiment = pipeline.getSentiment(comment.getText());
        comment.addSentiment(new Sentiment(sentiment));

        List<Taxonomy> taxonomyList = taxonomyModelWrapper.getTaxonomyFromModels(comment.getText());
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

        logger.trace("TaxonomyCategory Repository");
        taxonomyCategoryRepository.findAll().forEach(item -> {
            logger.trace(JsonHelper.write(item));
        });

        return sentiment;
    }

    @GetMapping ("/check")
    public String check() {
        return "NLPControllerMVC";
    }

}
