package com.kenrui.nlp.common.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.kenrui.nlp.common.jointables.CommentModelCategory;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Getter @Setter @NoArgsConstructor
public class TaxonomyCategory {
    @Column(name = "categoryId", table = "TaxonomyCategory", nullable = false)
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequence-generator")
    @SequenceGenerator(name = "sequence-generator", sequenceName = "taxonomy_category_sequence")
    public Long categoryId;

    public String category;

    // This is for categories predicted by models
    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonBackReference // https://stackoverflow.com/questions/3325387/infinite-recursion-with-jackson-json-and-hibernate-jpa-issue
    public List<CommentModelCategory> commentsModels = new ArrayList<>();

    // This is for user entered category
    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
    @JsonBackReference // https://stackoverflow.com/questions/3325387/infinite-recursion-with-jackson-json-and-hibernate-jpa-issue
    public List<Comment> comments = new ArrayList<>();

    public TaxonomyCategory(String category) {
        this.category = category;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        TaxonomyCategory that = (TaxonomyCategory) o;
        return Objects.equals(categoryId, that.categoryId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(categoryId);
    }

    public List<CommentModelCategory> getCommentsModels() {
        return commentsModels;
    }

    public void setCommentsModels(List<CommentModelCategory> commentsModels) {
        this.commentsModels = commentsModels;
    }

    public Long getId() {
        return categoryId;
    }

    public void setId(Long id) {
        this.categoryId = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

}
