package com.swm.cbz.repository;

import com.swm.cbz.domain.CategoryVideo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface CategoryVideoRepository extends JpaRepository<CategoryVideo, Long> {
}
