package com.kenrui.nlp.common.entities;

public class Taxonomy {
    protected TaxonomyCategory taxonomyCategory;
    protected TaxonomyProduct taxonomyProduct;

    public Taxonomy(TaxonomyCategory taxonomyCategory, TaxonomyProduct taxonomyProduct) {
        this.taxonomyCategory = taxonomyCategory;
        this.taxonomyProduct = taxonomyProduct;
    }

    public TaxonomyCategory getTaxonomyCategory() {
        return taxonomyCategory;
    }

    public void setTaxonomyCategory(TaxonomyCategory taxonomyCategory) {
        this.taxonomyCategory = taxonomyCategory;
    }

    public TaxonomyProduct getTaxonomyProduct() {
        return taxonomyProduct;
    }

    public void setTaxonomyProduct(TaxonomyProduct taxonomyProduct) {
        this.taxonomyProduct = taxonomyProduct;
    }
}
