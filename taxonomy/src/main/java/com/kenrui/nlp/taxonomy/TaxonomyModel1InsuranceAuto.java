package com.kenrui.nlp.taxonomy;

import com.kenrui.nlp.common.entities.Taxonomy;

public class TaxonomyModel1InsuranceAuto implements TaxonomyModel {

    @Override
    public Taxonomy getTaxonomy(String comment) {
        return new Taxonomy("Insurance", "Auto", this.getClass().getSimpleName());
    }
}
