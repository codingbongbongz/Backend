package com.swm.cbz.domain;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.swm.cbz.dto.authorization.request.SignupRequestDTO;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;

@Getter
@Setter
@Entity
@Table
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "users_id")
    private Long userId;

    @Column
    private String name;

    @Column
    private String password;

    @Column
    @NotNull
    private String email;

    @Column
    private String nickname;

    @ManyToOne
    @JoinColumn(name = "country_id")
    private Country country;

    @Column
    private String social;

    @Column
    private String profileImageUrl;

    @Column(length = 1500)
    private String introduce;

    @Column
    private Long totalScore = 0L;

    @Column
    @CreatedDate
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "users", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private Set<UserVideo> userVideos = new HashSet<>();

    @OneToMany(mappedBy = "users", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private Set<Evaluation> evaluations = new HashSet<>();

    public static Users of(SignupRequestDTO requestDTO) {
        return Users.builder()
                .name(requestDTO.getName())
                .nickname(requestDTO.getNickname())
                .email(requestDTO.getEmail())
                .introduce(requestDTO.getIntroduce())
                .password(requestDTO.getPassword())
                .totalScore(0L)
                .createdAt(LocalDateTime.now())
                .build();
    }
}
