package com.kenrui.nlp.nlpcore;

import com.kenrui.nlp.common.entities.*;
import com.kenrui.nlp.common.repositories.*;
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

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Optional;


// Really should move this class to the com.kenrui.nlp.common package
@Component("NLPControllerMVC")
@CrossOrigin
@RestController
public class NLPControllerMVC {
    private static final Logger logger = LogManager.getLogger("NLP");

    @Autowired private CommentRepository commentRepository;
    @Autowired private ModelRepository modelRepository;
    @Autowired private SentimentRepository sentimentRepository;
    @Autowired private TaxonomyCategoryRepository taxonomyCategoryRepository;
    @Autowired private TaxonomyProductRepository taxonomyProductRepository;

    private Pipeline pipeline = new Pipeline();
    TaxonomyModelWrapper taxonomyModelWrapper;


    public void dumpTables() {
        logger.trace("Comment Repository");
        commentRepository.findAll().forEach(item -> {
            logger.trace(JsonHelper.write(item));
        });

        logger.trace("Sentiment Repository");
        sentimentRepository.findAll().forEach(item -> {
            logger.trace(JsonHelper.write(item));
        });

        logger.trace("Model Repository");
        modelRepository.findAll().forEach(item -> {
            logger.trace(JsonHelper.write(item));
        });

        logger.trace("TaxonomyCategory Repository");
        taxonomyCategoryRepository.findAll().forEach(item -> {
            logger.trace(JsonHelper.write(item));
        });

        logger.trace("TaxonomyProduct Repository");
        taxonomyProductRepository.findAll().forEach(item -> {
            logger.trace(JsonHelper.write(item));
        });
    }

    public NLPControllerMVC() {
    }

    @PostConstruct
    public void init() {
        DatabaseInitializer databaseInitializer = new DatabaseInitializer(modelRepository,
                sentimentRepository, taxonomyCategoryRepository, taxonomyProductRepository);
        databaseInitializer.initialize();
        logger.trace("----------------------------------------------------------------------");
        logger.trace("After initialize");
        logger.trace("----------------------------------------------------------------------");
        dumpTables();

        // TaxonomyModelWrapper creates the classes for the different models used.
        // It needs to load the model objects from database so will need to wait until Spring fully boots up
        taxonomyModelWrapper = new TaxonomyModelWrapper(modelRepository,
                taxonomyCategoryRepository, taxonomyProductRepository);
    }

    @PostMapping("/postComment")
    public ResponseEntity postComment(@RequestBody Comment comment) {
        return new ResponseEntity(JsonHelper.write(comment), HttpStatus.OK);
    }

    @PostMapping("/postItem")
    public ResponseEntity postComment(@RequestBody ItemSubmitted itemSubmitted) {
        logger.trace("----------------------------------------------------------------------");
        logger.trace("/postItem");
        logger.trace("----------------------------------------------------------------------");

        Comment comment = new Comment(itemSubmitted.getText(), itemSubmitted.getPostedBy());

        // Guesstimate sentiment of comment posted
        String sentimentText = pipeline.getSentiment(comment.getText()).toLowerCase();

        // Need to fetch sentiment object based on text returned
        Sentiment sentiment = sentimentRepository.findBySentiment(sentimentText);
        comment.addSentiment(sentiment);

        if (itemSubmitted.getComplaint()) {
            // If it is a complaint we need operator to specify taxonomy details as well so
            // we can tell how accurate the AI models are doing over time
            if (itemSubmitted.getDetails().isEmpty()) {
                return new ResponseEntity(JsonHelper.write(itemSubmitted), HttpStatus.NOT_ACCEPTABLE);
            } else {
                itemSubmitted.getDetails().forEach((taxonomyItem) -> {
                    TaxonomyCategory userSuppliedCategory;
                    TaxonomyProduct userSuppliedProduct;

                    switch(taxonomyItem.getId().intValue()) {
                        case 0:
                            Optional<TaxonomyCategory> taxonomyCategoryOptional =
                                    taxonomyCategoryRepository.findById(taxonomyItem.getSelectedItemId());
                            if (taxonomyCategoryOptional.isPresent()) {
                                userSuppliedCategory = taxonomyCategoryOptional.get();
                                comment.addCategory(userSuppliedCategory);
                            }
                            break;
                        case 1:
                            Optional<TaxonomyProduct> taxonomyProductOptional =
                                    taxonomyProductRepository.findById(taxonomyItem.getSelectedItemId());
                            if (taxonomyProductOptional.isPresent()) {
                                userSuppliedProduct = taxonomyProductOptional.get();
                                comment.addProduct(userSuppliedProduct);
                            }
                            break;
                    }
                });

                // Use AI models to guesstimate category and product taxonomies
                // One Taxonomy object is returned for each AI model used
                // Each Taxonomy object contains a TaxonomyCategory object and a TaxonomyProduct object
                List<Taxonomy> taxonomyList = taxonomyModelWrapper.getTaxonomyFromModels(comment.getText());
                commentRepository.save(comment);
                comment.addTaxonomies(taxonomyList);
            }
        } else {
            // If it is not a complaint we consider this as a feedback and only do sentiment analysis which is already done
        }

        // Saves everything
        // - Comment along with links to user supplied category and product, and link to sentiment guessed to table Comment.
        // - One entry per taxonomy model used to guess category to join table CommentModelCategory.
        // - One entry per taxonomy model used to guess product to join table CommentModelProduct.
        commentRepository.save(comment);

        dumpTables();

        // Returns complete json record showing what has been created to front end
        return new ResponseEntity(JsonHelper.write(comment), HttpStatus.OK);
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

        logger.trace("Model Repository");
        modelRepository.findAll().forEach(item -> {
            logger.trace(JsonHelper.write(item));
        });

        logger.trace("TaxonomyCategory Repository");
        taxonomyCategoryRepository.findAll().forEach(item -> {
            logger.trace(JsonHelper.write(item));
        });

        logger.trace("TaxonomyProduct Repository");
        taxonomyProductRepository.findAll().forEach(item -> {
            logger.trace(JsonHelper.write(item));
        });

        return sentiment;
    }

    @GetMapping ("/check")
    public String check() {
        return "NLPControllerMVC";
    }

}
