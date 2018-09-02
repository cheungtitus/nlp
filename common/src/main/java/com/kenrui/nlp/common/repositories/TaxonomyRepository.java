package com.kenrui.nlp.common.repositories;

import com.kenrui.nlp.common.entities.Taxonomy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaxonomyRepository extends JpaRepository<Taxonomy, Long> {
}
