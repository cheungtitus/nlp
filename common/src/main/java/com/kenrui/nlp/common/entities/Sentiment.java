package com.kenrui.nlp.common.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter @Setter @NoArgsConstructor
public class Sentiment {
    @Column(name="sentiment_id",table="Sentiment",nullable=false)
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequence-generator")
    @SequenceGenerator(name = "sequence-generator", sequenceName = "sentiment_sequence")
    public Long sentiment_id;

    public String sentiment;

//    @ManyToOne(optional=false,cascade={CascadeType.ALL},targetEntity=Comment.class)
//    @JoinColumn(name="comment_id")
//    @JsonBackReference  // https://stackoverflow.com/questions/3325387/infinite-recursion-with-jackson-json-and-hibernate-jpa-issue
    @OneToMany(mappedBy = "sentiment", targetEntity = Comment.class)
    private Comment comment;

    public Sentiment(String sentiment) {
        this.sentiment = sentiment;
    }

    public void setComment(Comment comment) {
        this.comment = comment;
    }
}
