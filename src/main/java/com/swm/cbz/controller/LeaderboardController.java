package com.swm.cbz.controller;

import com.swm.cbz.common.response.ApiResponse;
import com.swm.cbz.common.response.SuccessMessage;
import com.swm.cbz.dto.LeaderboardDTO;
import com.swm.cbz.repository.UserRepository;
import com.swm.cbz.repository.UserVideoRepository;
import com.swm.cbz.service.LeaderboardService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class LeaderboardController {
    private final UserVideoRepository userVideoRepository;
    private final UserRepository userRepository;

    private final LeaderboardService leaderboardService;
    public LeaderboardController(UserVideoRepository userVideoRepository,
                                 UserRepository userRepository, LeaderboardService leaderboardService) {
        this.userVideoRepository = userVideoRepository;
        this.userRepository = userRepository;
        this.leaderboardService = leaderboardService;
    }

    @GetMapping("/leaderboard/video")
    public ApiResponse<List<LeaderboardDTO>> getLeaderboardByVideo() {
        List<LeaderboardDTO> leaderboardData = leaderboardService.getLeaderboardData();
        return ApiResponse.success(SuccessMessage.LEADERBOARD_SUCCESS, leaderboardData);
    }

    @GetMapping("/leaderboard/evaluation")
    public ApiResponse<List<LeaderboardDTO>> getLeaderboardByEvaluation(){
        List<LeaderboardDTO> leaderboardData = leaderboardService.getEvaluationLeaderboardData();
        return ApiResponse.success(SuccessMessage.LEADERBOARD_SUCCESS, leaderboardData);
    }
}
