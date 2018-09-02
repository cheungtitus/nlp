package com.kenrui.nlp.taxonomy;

import com.kenrui.nlp.common.entities.Taxonomy;

public class TaxonomyModel3MortgageHome implements TaxonomyModel {

    @Override
    public Taxonomy getTaxonomy(String comment) {
        return new Taxonomy("Mortgage", "Home", this.getClass().getSimpleName());
    }
}
