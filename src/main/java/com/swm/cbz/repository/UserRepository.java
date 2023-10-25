package com.swm.cbz.repository;

import com.swm.cbz.domain.Users;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface UserRepository extends JpaRepository<Users, Long> {

    Optional<Users> findByPassword(String password);

    Optional<Users> findByNickname(String nickname);

    Optional<Users> findByEmail(String email);

    @Query("SELECT u " + "FROM Users u " +
        "ORDER BY u.totalScore DESC")
    List<Users> findLeaderboardByTotalScore();
}
