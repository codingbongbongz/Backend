package com.swm.cbz.controller;

import com.swm.cbz.common.response.ApiResponse;
import com.swm.cbz.common.response.ErrorMessage;
import com.swm.cbz.common.response.SuccessMessage;
import com.swm.cbz.dto.LeaderboardDTO;
import com.swm.cbz.repository.UserRepository;
import com.swm.cbz.repository.UserVideoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class LeaderboardController {
    private final UserVideoRepository userVideoRepository;
    private final UserRepository userRepository;

    public LeaderboardController(UserVideoRepository userVideoRepository,
                                 UserRepository userRepository) {
        this.userVideoRepository = userVideoRepository;
        this.userRepository = userRepository;
    }

    @GetMapping("/leaderboard/video")
    public ApiResponse<List<LeaderboardDTO>> getLeaderboardByVideo() {
        List<LeaderboardDTO> leaderboardData = userVideoRepository.findLeaderboardData();
        return ApiResponse.success(SuccessMessage.LEADERBOARD_SUCCESS, leaderboardData);
    }

    @GetMapping("/leaderboard/evaluation")
    public ApiResponse<List<LeaderboardDTO>> getLeaderboardByEvaluation(){
        List<LeaderboardDTO> leaderboardData = userRepository.findLeaderboardByTotalScore();
        return ApiResponse.success(SuccessMessage.LEADERBOARD_SUCCESS, leaderboardData);
    }
}
