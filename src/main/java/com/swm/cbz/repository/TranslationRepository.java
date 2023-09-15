package com.swm.cbz.repository;

import com.swm.cbz.domain.Transcript;
import com.swm.cbz.domain.Translation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource
public interface TranslationRepository extends JpaRepository<Translation, Long> {
    List<Translation> findByTranscript_TranscriptId(Long transcriptId);
}
