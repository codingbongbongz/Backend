package com.swm.cbz.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table
public class Examples {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="example_id")
    private Long exampleId;

    @Column
    private String sentence;

    @ManyToOne
    @JoinColumn(name = "word_id", nullable = false)
    @JsonBackReference
    private Word word;
}
