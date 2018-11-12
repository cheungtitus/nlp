package com.kenrui.nlp.common.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter @Setter @NoArgsConstructor
public class TaxonomyProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequence-generator")
    @SequenceGenerator(name = "sequence-generator", sequenceName = "taxonomy_product_sequence")
    public Long id;

    public String product;
    public String model;

    @ManyToOne(optional=false,cascade={CascadeType.ALL},targetEntity=Comment.class)
    @JoinColumn(name="comment_id")
    @JsonBackReference
    // https://stackoverflow.com/questions/3325387/infinite-recursion-with-jackson-json-and-hibernate-jpa-issue
    private Comment comment;

    public TaxonomyProduct(String product, String model) {
        this.product = product;
        this.model = model;
    }

    public void setComment(Comment comment) {
        this.comment = comment;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public Comment getComment() {
        return comment;
    }
}
