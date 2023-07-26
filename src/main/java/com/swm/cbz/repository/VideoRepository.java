package com.swm.cbz.repository;

import com.swm.cbz.domain.Video;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface VideoRepository extends JpaRepository<Video, Long> {
}
