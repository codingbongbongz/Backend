package com.swm.cbz.repository;

import com.swm.cbz.domain.Country;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface CountryRepository extends JpaRepository<Country, Long> {
    Country findByCountryCode(String countryCode);
}
