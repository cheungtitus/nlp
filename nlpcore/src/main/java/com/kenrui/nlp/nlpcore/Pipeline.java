package com.kenrui.nlp.nlpcore;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations;
import edu.stanford.nlp.util.CoreMap;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Properties;

public class Pipeline {
    private static final Logger logger = LogManager.getLogger("NLP");

    private StanfordCoreNLP tokenizer;
    private StanfordCoreNLP pipeline;
    private String parserModel = null;
    private String sentimentModel = null;
    private Properties pipelineProps = new Properties();
    private Properties tokenizerProps = new Properties();

    public Pipeline() {
        if (sentimentModel != null) {
            pipelineProps.setProperty("sentiment.model", sentimentModel);
        }
        if (parserModel != null) {
            pipelineProps.setProperty("parse.model", parserModel);
        }

        pipelineProps.setProperty("annotators", "parse, sentiment");
        pipelineProps.setProperty("parse.binaryTrees", "true");
        pipelineProps.setProperty("parse.buildgraphs", "false");
        pipelineProps.setProperty("enforceRequirements", "false");
        tokenizerProps.setProperty("annotators", "tokenize, ssplit");

        if (tokenizerProps != null) {
            tokenizerProps.setProperty(StanfordCoreNLP.NEWLINE_SPLITTER_PROPERTY, "true");
        }

        tokenizer = new StanfordCoreNLP(tokenizerProps);
        pipeline = new StanfordCoreNLP(pipelineProps);
    }

    public String getSentiment(String text) {
        text = text.trim();
        String sentiment = "";

        if ( ! text.isEmpty()) {
            Annotation annotation = tokenizer.process(text);
            pipeline.annotate(annotation);
            for (CoreMap sentence : annotation.get(CoreAnnotations.SentencesAnnotation.class)) {
                sentiment =  sentence.get(SentimentCoreAnnotations.SentimentClass.class);
            }
        } else {
            sentiment = "";
        }

        return sentiment;
    }

}