package com.swm.cbz.repository;

import com.swm.cbz.domain.UserVideo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource
public interface UserVideoRepository extends JpaRepository<UserVideo,Long> {
    List<UserVideo> findByUserId(Long userId);

}
