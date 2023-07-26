package com.swm.cbz.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@Entity
@Table
public class Evaluation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="evaluation_id")
    private Long evaluationId;

    @Column
    private Long score;

    @Column
    private Long pronunciation;

    @Column
    private Long fluency;

    @Column
    private Long integrity;

    @Column
    private Long rhythm;

    @Column
    private Long speed;

    @Column
    private Date createdAt;

    @ManyToOne
    @JoinColumn(name = "transcript_id", nullable = false)
    private Transcript transcript;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

}
