package com.swm.cbz.repository;

import com.swm.cbz.domain.UserVideo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface UserVideoRepository extends JpaRepository<UserVideo,Long> {
}
