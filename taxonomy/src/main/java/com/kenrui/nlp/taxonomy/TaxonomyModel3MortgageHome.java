package com.kenrui.nlp.taxonomy;

import com.kenrui.nlp.common.entities.Taxonomy;
import com.kenrui.nlp.common.entities.TaxonomyCategory;
import com.kenrui.nlp.common.entities.TaxonomyProduct;

public class TaxonomyModel3MortgageHome implements TaxonomyModel {

    @Override
    public Taxonomy getTaxonomy(String comment) {
        String model = this.getClass().getSimpleName();
        TaxonomyCategory taxonomyCategory = new TaxonomyCategory("Mortgage", model);
        TaxonomyProduct taxonomyProduct = new TaxonomyProduct("Home", model);
        return new Taxonomy(taxonomyCategory, taxonomyProduct);
    }
}
