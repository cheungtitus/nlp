package com.kenrui.nlp.taxonomy;

import com.kenrui.nlp.common.entities.Taxonomy;

public interface TaxonomyModel {
    public Taxonomy getTaxonomy(String comment);
}
