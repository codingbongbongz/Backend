package com.swm.cbz.repository;

import com.swm.cbz.domain.Video;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource
public interface VideoRepository extends JpaRepository<Video, Long> {

    List<Video> findTop5ByOrderByYoutubeViewsDesc();

    Video findByLink(String link);

    boolean existsByLink(String link);
}
