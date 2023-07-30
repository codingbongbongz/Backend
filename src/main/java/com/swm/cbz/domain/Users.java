package com.swm.cbz.domain;

import com.fasterxml.jackson.annotation.JsonManagedReference;
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
public class Users {
    @Id
    @Column(name="users_id")
    private String usersId;

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

    @OneToMany(mappedBy = "users", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private Set<UserVideo> userVideos = new HashSet<>();

    @OneToMany(mappedBy = "users", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private Set<Evaluation> evaluations = new HashSet<>();
}
