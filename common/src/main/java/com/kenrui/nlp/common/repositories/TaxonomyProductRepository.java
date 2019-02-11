package com.kenrui.nlp.common.repositories;

import com.kenrui.nlp.common.entities.TaxonomyProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaxonomyProductRepository extends JpaRepository<TaxonomyProduct, Long> {
    TaxonomyProduct findByProduct(String productName);
}
