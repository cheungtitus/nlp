package com.kenrui.nlp.common.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Comment {
    @Column(name = "comment_id", table = "Comment", nullable = false)
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequence-generator")
    @SequenceGenerator(name = "sequence-generator", sequenceName = "comment_sequence")
    private Long comment_id;

    private String text;
    private String postedBy;

    //    @OneToMany(cascade={CascadeType.ALL},targetEntity=Sentiment.class,mappedBy="comment")
//    private Collection<Sentiment> sentiments = new ArrayList();
    @ManyToOne(optional = false)
    @JoinColumn(name = "sentiment_id", referencedColumnName = "sentiment_id")
    private Sentiment sentiment;

    //    @OneToMany(cascade={CascadeType.ALL},targetEntity= TaxonomyCategory.class,mappedBy="comment")
//    private Collection<TaxonomyCategory> taxonomyCategories = new ArrayList();
    @ManyToOne(optional = false)
    @JoinColumn(name = "id", referencedColumnName = "id")
    private TaxonomyCategory taxonomyCategory;

    //    @OneToMany(cascade={CascadeType.ALL},targetEntity= TaxonomyProduct.class,mappedBy="comment")
//    private Collection<TaxonomyProduct> taxonomyProducts = new ArrayList();
    @ManyToOne(optional = false)
    @JoinColumn(name = "id", referencedColumnName = "id")
    private TaxonomyProduct taxonomyProduct;

    public Comment(String text, String postedBy) {
        this.text = text;
        this.postedBy = postedBy;
    }

    public String getText() {
        return text;
    }

    public void addSentiment(Sentiment sentiment) {
        sentiment.setComment(this);
        sentiments.add(sentiment);
    }

    public void removeSentiment(Sentiment sentiment) {
        sentiment.setComment(null);
        sentiments.remove(sentiment);
    }

    public void addTaxonomyCategory(TaxonomyCategory taxonomyCategory) {
        taxonomyCategory.setComment(this);
        taxonomyCategories.add(taxonomyCategory);
    }

    public void removeTaxonomyCategory(TaxonomyCategory taxonomyCategory) {
        taxonomyCategory.setComment(null);
        taxonomyCategories.remove(taxonomyCategory);
    }

    public void addTaxonomyProduct(TaxonomyProduct taxonomyProduct) {
        taxonomyProduct.setComment(this);
        taxonomyProducts.add(taxonomyProduct);
    }

    public void removeTaxonomyProduct(TaxonomyProduct taxonomyProduct) {
        taxonomyProduct.setComment(null);
        taxonomyProducts.remove(taxonomyProduct);
    }

    public void addTaxonomies(List<Taxonomy> taxonomyList) {
        taxonomyList.forEach(taxonomy -> {
            taxonomy.getTaxonomyCategory().setComment(this);
            taxonomyCategories.add(taxonomy.getTaxonomyCategory());
            taxonomy.getTaxonomyProduct().setComment(this);
            taxonomyProducts.add(taxonomy.getTaxonomyProduct());
        });
    }
}
