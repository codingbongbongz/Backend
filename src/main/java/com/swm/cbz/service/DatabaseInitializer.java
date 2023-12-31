package com.swm.cbz.service;

import com.swm.cbz.domain.Users;
import com.swm.cbz.dto.LeaderboardDTO;
import com.swm.cbz.repository.UserRepository;
import com.swm.cbz.repository.UserVideoRepository;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class DatabaseInitializer {

    @Autowired
    private UserVideoRepository userVideoRepository;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private StringRedisTemplate redisTemplate;

    @EventListener(ApplicationReadyEvent.class)
    public void initializeLeaderboard() {
        List<LeaderboardDTO> leaderboardData = userVideoRepository.findLeaderboardData();
        List<Users> evaluationLeaderboardUser = userRepository.findLeaderboardByTotalScore();

        List<LeaderboardDTO> evaluationLeaderboard = evaluationLeaderboardUser.stream()
            .map(it -> new LeaderboardDTO(it.getUserId(), it.getTotalScore()))
            .collect(Collectors.toList());

        leaderboardData.forEach(entry -> {
            redisTemplate.opsForZSet()
                .add("video:leaderboard", entry.getUserId().toString(), entry.getPoint());
        });
        evaluationLeaderboard.forEach(entry -> {
            redisTemplate.opsForZSet()
                .add("evaluation:leaderboard", entry.getUserId().toString(), entry.getPoint());
        });
    }
}
