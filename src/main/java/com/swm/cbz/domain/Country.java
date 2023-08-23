package com.swm.cbz.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table
public class Country {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="country_id")
    private Long countryId;

    @Column(unique = true)
    private String countryCode;

    @Column
    private String countryName;
}
