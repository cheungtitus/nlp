package com.kenrui.nlp.common.entities;

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
public class Comment {
    @Column(name = "commentId", table = "Comment", nullable = false)
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequence-generator")
    @SequenceGenerator(name = "sequence-generator", sequenceName = "comment_sequence")
    public Long commentId;

    public String text;
    public String postedBy;

    @OneToMany(mappedBy = "comment", cascade = CascadeType.ALL, orphanRemoval = true)
    public List<CommentModelCategory> modelsCategories = new ArrayList<>();

    @OneToMany(mappedBy = "comment", cascade = CascadeType.ALL, orphanRemoval = true)
    public List<CommentModelProduct> modelsProducts = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "sentimentId")
    public Sentiment sentiment;

    @ManyToOne
    @JoinColumn(name = "categoryId")
    public TaxonomyCategory category;

    @ManyToOne
    @JoinColumn(name = "productId")
    public TaxonomyProduct product;

    public Comment(String text, String postedBy) {
        this.text = text;
        this.postedBy = postedBy;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        Comment that = (Comment) o;
        return Objects.equals(commentId, that.commentId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(commentId);
    }

    // Comments posted creates a relationship with categories via the CommentModelCategory join table
    public void addModelCategory(Model model, TaxonomyCategory category) {
        CommentModelCategory commentModelCategory = new CommentModelCategory(this, model, category);
        modelsCategories.add(commentModelCategory);
//        model.getCommentsCategories().add(commentModelCategory);
//        category.getCommentsModels().add(commentModelCategory);
    }

    // Comments posted creates a relationship with products via the CommentModelProduct join table
    public void addModelProduct(Model model, TaxonomyProduct product) {
        CommentModelProduct commentModelProduct = new CommentModelProduct(this, model, product);
        modelsProducts.add(commentModelProduct);
//        model.getCommentsProducts().add(commentModelProduct);
//        product.getCommentsModels().add(commentModelProduct);
    }

    // Comments posted creates a relationship with sentiment
    public void addSentiment(Sentiment sentiment) {
        this.sentiment = sentiment;
    }

    // Comments posted creates a relationship with category supplied by user
    public void addCategory(TaxonomyCategory category) {
        this.category = category;
    }

    // Comments posted creates a relationship with product supplied by user
    public void addProduct(TaxonomyProduct product) {
        this.product = product;
    }

    // Add links to categories and products guesstimated from each AI model
    public void addTaxonomies(List<Taxonomy> taxonomyList) {
        taxonomyList.forEach(taxonomy -> {
            Model model = taxonomy.getModel();
            TaxonomyCategory taxonomyCategory = taxonomy.getTaxonomyCategory();
            TaxonomyProduct taxonomyProduct = taxonomy.getTaxonomyProduct();

            this.addModelCategory(model, taxonomyCategory);
            this.addModelProduct(model, taxonomyProduct);
        });
    }

    public Long getCommentId() {
        return commentId;
    }

    public void setCommentId(Long commentId) {
        this.commentId = commentId;
    }

    public String getText() {
        return text;
    }

    public Sentiment getSentiment() {
        return sentiment;
    }

    public TaxonomyCategory getCategory() {
        return category;
    }

    public TaxonomyProduct getProduct() {
        return product;
    }
}
