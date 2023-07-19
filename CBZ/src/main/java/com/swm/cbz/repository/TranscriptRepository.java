package com.swm.cbz.repository;

import com.swm.cbz.domain.Transcript;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface TranscriptRepository extends JpaRepository<Transcript, Long> {
}
