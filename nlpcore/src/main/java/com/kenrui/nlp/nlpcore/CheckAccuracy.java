package com.kenrui.nlp.nlpcore;

import com.kenrui.nlp.common.jointables.CommentModelCategory;
import com.kenrui.nlp.common.jointables.CommentModelProduct;
import com.kenrui.nlp.common.repositories.CommentRepository;
import com.kenrui.nlp.common.repositories.ModelRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CheckAccuracy {
    public CommentRepository commentRepository;
    public ModelRepository modelRepository;
    public Map<String, List<Double>> modelScores;

    public CheckAccuracy(CommentRepository commentRepository, ModelRepository modelRepository) {
        this.commentRepository = commentRepository;
        this.modelRepository = modelRepository;
        this.modelScores = modelScores = new HashMap<String, List<Double>>();
        this.modelScores.put("Category", new ArrayList<>());
        this.modelScores.put("Product", new ArrayList<>());
        Long modelsUsed = modelRepository.count();
        for (int i = 0; i < modelsUsed.intValue(); i++) {
            this.modelScores.get("Category").add(0, 0.0);
            this.modelScores.get("Product").add(0, 0.0);
        }

    }

    public Map<String, List<Double>> checkScore() {

        commentRepository.findAll().forEach((comment) -> {

            Long enteredCategory = comment.getCategory().getId();
            Long enteredProduct = comment.getProduct().getId();

            List<CommentModelCategory> commentModelCategory = comment.modelsCategories;
            commentModelCategory.forEach((modelCategory) -> {
                Long modelUsed = modelCategory.getModel().getModelId();
                Long categoryPredicted = modelCategory.getCategory().getId();

                if (enteredCategory == categoryPredicted) {
                    Double newScore = modelScores.get("Category").get(modelUsed.intValue() - 1) + 1;
                    modelScores.get("Category").set(modelUsed.intValue() - 1, newScore);
                }
            });

            List<CommentModelProduct> commentModelProduct = comment.modelsProducts;
            commentModelProduct.forEach((modelProduct) -> {
                Long modelUsed = modelProduct.getModel().getModelId();
                Long productPredicted = modelProduct.getProduct().getId();

                if (enteredProduct == productPredicted) {
                    Double newScore = modelScores.get("Product").get(modelUsed.intValue() - 1) + 1;
                    modelScores.get("Product").set(modelUsed.intValue() - 1, newScore);
                }
            });

        });

        Long totalComments = commentRepository.count();
        int index = 0;

        List<Double> modelsCategoryScores = modelScores.get("Category");
        for (Double score : modelsCategoryScores) {
            modelsCategoryScores.set(index, (score / totalComments) * 100);
            index++;
        }

        index = 0;
        List<Double> modelsProductScores = modelScores.get("Product");
        for (Double score : modelsProductScores) {
            modelsProductScores.set(index, (score / totalComments) * 100);
            index++;
        }

        return modelScores;
    }
}
