package com.kenrui.nlp.common.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter @Setter @NoArgsConstructor
public class Taxonomy {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequence-generator")
    @SequenceGenerator(name = "sequence-generator", sequenceName = "taxonomy_sequence")
    public Long id;

    public String category;
    public String product;
    public String model;

    @ManyToOne(optional=false,cascade={CascadeType.ALL},targetEntity=Comment.class)
    @JoinColumn(name="comment_id")
    @JsonBackReference
    // https://stackoverflow.com/questions/3325387/infinite-recursion-with-jackson-json-and-hibernate-jpa-issue
    private Comment comment;

    public Taxonomy(String category, String product, String model) {
        this.category = category;
        this.product = product;
        this.model = model;
    }

    public void setComment(Comment comment) {
        this.comment = comment;
    }
}
