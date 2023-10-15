package com.swm.cbz.service;

import com.swm.cbz.dto.LeaderboardDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class LeaderboardService {
    @Autowired
    private StringRedisTemplate redisTemplate;

    public void incrementUserScore(String userId) {
        redisTemplate.opsForZSet().incrementScore("video:leaderboard", userId, 1);
    }

    public List<LeaderboardDTO> getLeaderboardData() {
        Set<ZSetOperations.TypedTuple<String>> range = redisTemplate.opsForZSet()
                .reverseRangeWithScores("video:leaderboard", 0, -1); // all list

        assert range != null;
        return range.stream()
                .map(tuple -> new LeaderboardDTO(
                        Long.parseLong(tuple.getValue()),
                        Math.round(tuple.getScore())
                ))
                .collect(Collectors.toList());
    }

    public List<LeaderboardDTO> getEvaluationLeaderboardData() {
        Set<ZSetOperations.TypedTuple<String>> range = redisTemplate.opsForZSet()
                .reverseRangeWithScores("evaluation:leaderboard", 0, -1); // whole list

        assert range != null;
        return range.stream()
                .map(tuple -> new LeaderboardDTO(
                        Long.parseLong(tuple.getValue()),
                        Math.round(tuple.getScore())
                ))
                .collect(Collectors.toList());
    }
}
