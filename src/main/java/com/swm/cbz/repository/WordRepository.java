package com.swm.cbz.repository;

import com.swm.cbz.domain.Word;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface WordRepository extends JpaRepository<Word, Long> {
    boolean existsByWord(String noun);

    Word findByWord(String text);
}