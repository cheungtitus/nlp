package com.kenrui.nlp.common.jointables;

import com.kenrui.nlp.common.compositekeys.CommentModelId;
import com.kenrui.nlp.common.entities.Comment;
import com.kenrui.nlp.common.entities.Model;
import com.kenrui.nlp.common.entities.TaxonomyCategory;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class CommentModelCategory {
    @EmbeddedId
    public CommentModelId commentModelId;

    @ManyToOne
    @MapsId("commentId") // maps the commentId of the embedded id to a comment
    public Comment comment;

    @ManyToOne
    @MapsId("modelId") // maps the modelId of the embedded id to a model
    public Model model;

    @ManyToOne
    @JoinColumn(name = "categoryId")
    public TaxonomyCategory category;

    public CommentModelCategory(Comment comment, Model model, TaxonomyCategory category) {
        this.comment = comment;
        this.model = model;
        this.category = category;
        this.commentModelId = new CommentModelId(comment.getCommentId(), model.getModelId());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        CommentModelCategory that = (CommentModelCategory) o;
        return Objects.equals(comment, that.comment) && Objects.equals(model, that.model);
    }

    @Override
    public int hashCode() {
        return Objects.hash(comment, model);
    }
}
