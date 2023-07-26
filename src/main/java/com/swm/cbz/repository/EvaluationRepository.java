package com.swm.cbz.repository;

import com.swm.cbz.domain.Evaluation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface EvaluationRepository extends JpaRepository<Evaluation, Long> {
}
