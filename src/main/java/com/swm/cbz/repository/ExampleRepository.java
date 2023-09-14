package com.swm.cbz.repository;

import com.swm.cbz.domain.Evaluation;
import com.swm.cbz.domain.Examples;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource
public interface ExampleRepository extends JpaRepository<Examples, Long> {

}
