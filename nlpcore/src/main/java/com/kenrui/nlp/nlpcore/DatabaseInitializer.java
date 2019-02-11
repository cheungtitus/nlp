package com.kenrui.nlp.nlpcore;

import com.kenrui.nlp.common.entities.Model;
import com.kenrui.nlp.common.entities.Sentiment;
import com.kenrui.nlp.common.entities.TaxonomyCategory;
import com.kenrui.nlp.common.entities.TaxonomyProduct;
import com.kenrui.nlp.common.repositories.*;

public class DatabaseInitializer {

    private ModelRepository modelRepository;
    private SentimentRepository sentimentRepository;
    private TaxonomyCategoryRepository taxonomyCategoryRepository;
    private TaxonomyProductRepository taxonomyProductRepository;

    public DatabaseInitializer(ModelRepository modelRepository,
                               SentimentRepository sentimentRepository,
                               TaxonomyCategoryRepository taxonomyCategoryRepository,
                               TaxonomyProductRepository taxonomyProductRepository) {
        this.modelRepository = modelRepository;
        this.sentimentRepository = sentimentRepository;
        this.taxonomyCategoryRepository = taxonomyCategoryRepository;
        this.taxonomyProductRepository = taxonomyProductRepository;
    }

    public void initialize() {

        Sentiment positive = new Sentiment("positive");
        Sentiment neutral = new Sentiment("neutral");
        Sentiment negative = new Sentiment("negative");
        sentimentRepository.save(positive);
        sentimentRepository.save(neutral);
        sentimentRepository.save(negative);

        TaxonomyCategory insurance = new TaxonomyCategory("insurance");
        TaxonomyCategory mortgage = new TaxonomyCategory("mortgage");
        taxonomyCategoryRepository.save(insurance);
        taxonomyCategoryRepository.save(mortgage);

        TaxonomyProduct auto = new TaxonomyProduct("auto");
        TaxonomyProduct life = new TaxonomyProduct("life");
        TaxonomyProduct home = new TaxonomyProduct("home");
        taxonomyProductRepository.save(auto);
        taxonomyProductRepository.save(life);
        taxonomyProductRepository.save(home);

        Model model1 = new Model("model1");
        Model model2 = new Model("model2");
        Model model3 = new Model("model3");
        modelRepository.save(model1);
        modelRepository.save(model2);
        modelRepository.save(model3);
    }
}
