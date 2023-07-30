package com.swm.cbz.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table
public class CategoryVideo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="categoryvideo_id")
    private Long categoryVideoId;

    @ManyToOne
    @JoinColumn(name = "video_id", nullable = false)
    @JsonBackReference
    private Video video;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    @JsonBackReference
    private Category category;
}
