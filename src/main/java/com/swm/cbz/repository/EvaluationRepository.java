package com.swm.cbz.repository;

import com.swm.cbz.domain.Evaluation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;
import java.util.Optional;

@RepositoryRestResource
public interface EvaluationRepository extends JpaRepository<Evaluation, Long> {
    List<Evaluation> findByUsers_UserIdAndTranscript_TranscriptIdOrderByCreatedAtDesc(Long userId, Long transcriptId);
}
