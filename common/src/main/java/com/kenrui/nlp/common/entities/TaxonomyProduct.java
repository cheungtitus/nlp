package com.kenrui.nlp.common.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.kenrui.nlp.common.jointables.CommentModelProduct;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Getter @Setter @NoArgsConstructor
public class TaxonomyProduct {
    @Column(name = "productId", table = "TaxonomyProduct", nullable = false)
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequence-generator")
    @SequenceGenerator(name = "sequence-generator", sequenceName = "taxonomy_product_sequence")
    public Long productId;

    public String product;

    // This is for products predicted by models
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonBackReference
    // https://stackoverflow.com/questions/3325387/infinite-recursion-with-jackson-json-and-hibernate-jpa-issue
    public List<CommentModelProduct> commentsModels = new ArrayList<>();

    // This is for user entered product
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    @JsonBackReference // https://stackoverflow.com/questions/3325387/infinite-recursion-with-jackson-json-and-hibernate-jpa-issue
    public List<Comment> comments = new ArrayList<>();

    public TaxonomyProduct(String product) {
        this.product = product;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        TaxonomyProduct that = (TaxonomyProduct) o;
        return Objects.equals(productId, that.productId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(productId);
    }

    public List<CommentModelProduct> getCommentsModels() {
        return commentsModels;
    }

    public void setCommentsModels(List<CommentModelProduct> commentsModels) {
        this.commentsModels = commentsModels;
    }

    public Long getId() {
        return productId;
    }

    public void setId(Long id) {
        this.productId = id;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }
}
