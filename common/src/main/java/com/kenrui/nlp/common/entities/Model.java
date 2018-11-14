package com.kenrui.nlp.common.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.kenrui.nlp.common.jointables.CommentModelCategory;
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
public class Model {
    @Column(name = "modelId", table = "Model", nullable = false)
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequence-generator")
    @SequenceGenerator(name = "sequence-generator", sequenceName = "model_sequence")
    public Long modelId;

    public String model;

    @OneToMany(mappedBy = "model", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonBackReference // https://stackoverflow.com/questions/3325387/infinite-recursion-with-jackson-json-and-hibernate-jpa-issue
    public List<CommentModelCategory> commentsCategories = new ArrayList<>();

    @OneToMany(mappedBy = "model", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonBackReference // https://stackoverflow.com/questions/3325387/infinite-recursion-with-jackson-json-and-hibernate-jpa-issue
    public List<CommentModelProduct> commentsProducts = new ArrayList<>();

    public Model(String model) {
        this.model = model;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        Model that = (Model) o;
        return Objects.equals(modelId, that.modelId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(modelId);
    }

    public List<CommentModelCategory> getCommentsCategories() {
        return commentsCategories;
    }

    public void setCommentsCategories(List<CommentModelCategory> commentsCategories) {
        this.commentsCategories = commentsCategories;
    }

    public List<CommentModelProduct> getCommentsProducts() {
        return commentsProducts;
    }

    public void setCommentsProducts(List<CommentModelProduct> commentsProducts) {
        this.commentsProducts = commentsProducts;
    }

    public Long getModelId() {
        return modelId;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }
}
