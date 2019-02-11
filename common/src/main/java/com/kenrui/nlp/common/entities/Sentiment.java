package com.kenrui.nlp.common.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.NaturalId;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Getter @Setter @NoArgsConstructor
public class Sentiment {
    @Column(name="sentimentId",table="Sentiment",nullable=false)
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequence-generator")
    @SequenceGenerator(name = "sequence-generator", sequenceName = "sentiment_sequence")
    public Long sentimentId;

    @NaturalId
    @Column(nullable = false, unique =  true)
    public String sentiment;

    @OneToMany(mappedBy = "sentiment", cascade = CascadeType.ALL)
    @JsonBackReference // https://stackoverflow.com/questions/3325387/infinite-recursion-with-jackson-json-and-hibernate-jpa-issue
    public List<Comment> comments = new ArrayList<>();

    public Sentiment(String sentiment) {
        this.sentiment = sentiment;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        Sentiment that = (Sentiment) o;
        return Objects.equals(sentimentId, that.sentimentId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sentimentId);
    }
}
