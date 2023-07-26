package com.swm.cbz.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table
public class Transcript {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="transcript_id")
    private Long transcriptId;

    @Column
    private String sentence;

    @Column
    private Double start;

    @Column
    private Double duration;

    @Column
    private String soundLink;

    @ManyToOne
    @JoinColumn(name = "video_id", nullable = false)
    private Video video;

    @OneToMany(mappedBy = "transcript", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Evaluation> promptTags = new HashSet<>();

    @OneToMany(mappedBy = "transcript", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Translation> translations = new HashSet<>();
}
