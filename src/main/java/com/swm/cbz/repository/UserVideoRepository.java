package com.swm.cbz.repository;

import com.swm.cbz.domain.UserVideo;
import com.swm.cbz.domain.Users;
import com.swm.cbz.dto.LeaderboardDTO;
import org.apache.catalina.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;
import java.util.Optional;

@RepositoryRestResource
public interface UserVideoRepository extends JpaRepository<UserVideo,Long> {
    List<UserVideo> findByUsers(Users users);

    Optional<UserVideo> findByUsers_UserIdAndVideo_VideoId(Long userId, Long videoId);

    @Query("SELECT new com.swm.cbz.dto.LeaderboardDTO(u.users.userId, COUNT(u.video.videoId)) " +
            "FROM UserVideo u " +
            "GROUP BY u.users.userId " +
            "ORDER BY COUNT(u.video.videoId) DESC")
    List<LeaderboardDTO> findLeaderboardData();
}

