package com.swm.cbz.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table
public class User {
    @Id
    @Column(name="user_id")
    private String userId;

    @Column
    private String name;

    @Column
    private String password;

    @Column
    private String email;

    @Column
    private String nickname;

    @Column
    private String country;

    @Column
    private String social;

    @Column
    private Date createdAt;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<UserVideo> userVideos = new HashSet<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Evaluation> evaluations = new HashSet<>();
}
