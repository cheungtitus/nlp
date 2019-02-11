package com.kenrui.nlp.common.compositekeys;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
public class CommentModelId implements Serializable {
    protected Long commentId;
    protected Long modelId;

    public CommentModelId(Long commentId, Long modelId) {
        this.commentId = commentId;
        this.modelId = modelId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        CommentModelId that = (CommentModelId) o;
        return Objects.equals(commentId, that.commentId) && Objects.equals(modelId, that.modelId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(commentId, modelId);
    }
}
