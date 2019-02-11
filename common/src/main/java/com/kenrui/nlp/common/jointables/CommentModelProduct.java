package com.kenrui.nlp.common.jointables;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.kenrui.nlp.common.compositekeys.CommentModelId;
import com.kenrui.nlp.common.entities.Comment;
import com.kenrui.nlp.common.entities.Model;
import com.kenrui.nlp.common.entities.TaxonomyProduct;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class CommentModelProduct {
    @EmbeddedId
    public CommentModelId commentModelId;

    @ManyToOne
    @MapsId("commentId") // maps the commentId of the embedded id to a comment
    @JsonBackReference
    public Comment comment;

    @ManyToOne
    @MapsId("modelId") // maps the modelId of the embedded id to a model
    @JsonBackReference
    public Model model;

    @ManyToOne
    @JoinColumn(name = "productId")
    public TaxonomyProduct product;

    public CommentModelProduct(Comment comment, Model model, TaxonomyProduct product) {
        this.comment = comment;
        this.model = model;
        this.product = product;
        this.commentModelId = new CommentModelId(this.comment.getCommentId(), this.model.getModelId());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        CommentModelProduct that = (CommentModelProduct) o;
        return Objects.equals(comment, that.comment) && Objects.equals(model, that.model);
    }

    @Override
    public int hashCode() {
        return Objects.hash(comment, model);
    }

    public Model getModel() {
        return model;
    }

    public TaxonomyProduct getProduct() {
        return product;
    }
}
