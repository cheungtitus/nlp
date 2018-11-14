package com.kenrui.nlp.common.repositories;

import com.kenrui.nlp.common.entities.Model;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ModelRepository extends JpaRepository<Model, Long> {
    Model findByModel(String modelName);
}
