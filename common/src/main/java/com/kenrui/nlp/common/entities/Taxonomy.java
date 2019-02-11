package com.kenrui.nlp.common.entities;

public class Taxonomy {
    public Model model;
    public TaxonomyCategory taxonomyCategory;
    public TaxonomyProduct taxonomyProduct;

    public Taxonomy(Model model, TaxonomyCategory taxonomyCategory, TaxonomyProduct taxonomyProduct) {
        this.model = model;
        this.taxonomyCategory = taxonomyCategory;
        this.taxonomyProduct = taxonomyProduct;
    }

    public Model getModel() {
        return model;
    }

    public void setModel(Model model) {
        this.model = model;
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
