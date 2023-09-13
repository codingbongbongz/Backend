package com.swm.cbz.domain;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.util.*;

@Getter
@Setter
@Entity
@Table
public class Video {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="video_id")
    private Long videoId;

    @Column
    private String link;

    @Column
    private String videoTitle;

    @Column
    private String creator;

    @Column
    private Long duration;

    @Column
    private Boolean isDefault;

    @Column
    private Long views;

    @Column
    private Long youtubeViews;

    @Column
    @CreatedDate
    private Date createdAt;

    @OneToMany(mappedBy = "video", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private Set<CategoryVideo> categoryVideos = new HashSet<>();

    @OneToMany(mappedBy = "video", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private Set<UserVideo> userVideos = new HashSet<>();

    @OneToMany(mappedBy = "video", cascade = CascadeType.ALL,orphanRemoval = true)
    @JsonManagedReference
    private Set<Transcript> transcripts = new HashSet<>();
}
