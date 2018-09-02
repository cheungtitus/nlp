package com.kenrui.nlp.common.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Getter @Setter @NoArgsConstructor
public class Comment {
    @Column(name="comment_id",table="Comment",nullable=false)
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequence-generator")
    @SequenceGenerator(name = "sequence-generator", sequenceName = "comment_sequence")
    private Long comment_id;

    private String body;
    private String postedBy;

    @OneToMany(cascade={CascadeType.ALL},targetEntity=Sentiment.class,mappedBy="comment")
    private Collection<Sentiment> sentiments = new ArrayList();

    @OneToMany(cascade={CascadeType.ALL},targetEntity=Taxonomy.class,mappedBy="comment")
    private Collection<Taxonomy> taxonomies = new ArrayList();

    public Comment(String body, String postedBy) {
        this.body = body;
        this.postedBy = postedBy;
    }

    public String getBody() {
        return body;
    }

    public void addSentiment(Sentiment sentiment) {
        sentiment.setComment(this);
        sentiments.add(sentiment);
    }

    public void removeSentiment(Sentiment sentiment) {
        sentiment.setComment(null);
        sentiments.remove(sentiment);
    }

    public void addTaxonomy(Taxonomy taxonomy) {
        taxonomy.setComment(this);
        taxonomies.add(taxonomy);
    }

    public void removeTaxonomy(Taxonomy taxonomy) {
        taxonomy.setComment(null);
        taxonomies.remove(taxonomy);
    }

    public void addTaxonomies(List<Taxonomy> taxonomyList) {
        taxonomyList.forEach(taxonomy -> {
            taxonomy.setComment(this);
        });
        taxonomies.addAll(taxonomyList);
    }
}
