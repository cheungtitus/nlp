package com.kenrui.nlp.common.repositories;

import com.kenrui.nlp.common.entities.Sentiment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SentimentRepository extends JpaRepository<Sentiment, Long> {
}
