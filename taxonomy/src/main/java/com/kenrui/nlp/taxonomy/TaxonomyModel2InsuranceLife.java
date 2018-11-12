package com.kenrui.nlp.taxonomy;

import com.kenrui.nlp.common.entities.Taxonomy;
import com.kenrui.nlp.common.entities.TaxonomyCategory;
import com.kenrui.nlp.common.entities.TaxonomyProduct;

public class TaxonomyModel2InsuranceLife implements TaxonomyModel {

    @Override
    public Taxonomy getTaxonomy(String comment) {
        String model = this.getClass().getSimpleName();
        TaxonomyCategory taxonomyCategory = new TaxonomyCategory("Insurance", model);
        TaxonomyProduct taxonomyProduct = new TaxonomyProduct("Life", model);
        return new Taxonomy(taxonomyCategory, taxonomyProduct);
    }
}
