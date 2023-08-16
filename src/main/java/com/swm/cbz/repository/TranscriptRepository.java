package com.swm.cbz.repository;

import com.swm.cbz.domain.Transcript;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;
import java.util.Optional;

@RepositoryRestResource
public interface TranscriptRepository extends JpaRepository<Transcript, Long> {
    Optional<Transcript> findByTranscriptIdAndVideoVideoId(Long transcriptId, Long videoId);

    List<Transcript> findAllByVideo_VideoId(Long videoId);
}
