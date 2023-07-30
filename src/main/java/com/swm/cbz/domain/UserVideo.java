package com.swm.cbz.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@Entity
@Table
public class UserVideo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="uservideo_id")
    private Long userVideoId;

    @Column
    private Date createdAt;

    @Column
    private Date updatedAt;

    @ManyToOne
    @JoinColumn(name = "video_id", nullable = false)
    @JsonBackReference
    private Video video;

    @ManyToOne
    @JoinColumn(name = "users_id", nullable = false)
    @JsonBackReference
    private Users users;
}
