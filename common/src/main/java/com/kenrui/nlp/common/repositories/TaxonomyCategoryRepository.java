package com.kenrui.nlp.common.repositories;

import com.kenrui.nlp.common.entities.TaxonomyCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaxonomyCategoryRepository extends JpaRepository<TaxonomyCategory, Long> {
    TaxonomyCategory findByCategory(String categoryName);
}