package com.swm.cbz.controller;

import com.swm.cbz.common.response.ApiResponse;
import com.swm.cbz.common.response.ErrorMessage;
import com.swm.cbz.common.response.SuccessMessage;
import com.swm.cbz.service.OpenAIService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class OpenAIController {


    private final OpenAIService openAIService;

    public OpenAIController(OpenAIService openAIService) {
        this.openAIService = openAIService;
    }

    @PostMapping(value = "/openAI/examples")
    public ApiResponse<Map<String, Object>> fetchExamples(@RequestParam String word) {
        Map<String, Object> responseData = new HashMap<>();
        try {
            List<String> examples = openAIService.fetchExamples(word);
            responseData.put("examples", examples);
            return ApiResponse.success(SuccessMessage.OPENAI_SUCCESS, responseData);
        } catch (Exception e) {
            return ApiResponse.error(ErrorMessage.OPENAI_FAILURE);
        }
    }

    @PostMapping(value = "/openAI/nouns")
    public ApiResponse<Map<String, Object>> fetchNouns(@RequestParam String sentence) {
        Map<String, Object> responseData = new HashMap<>();
        String inputText = "Give me the nouns in this Korean sentence separated by blank spaces: '" + sentence + "'";

        try {
            List<String> nouns = openAIService.fetchNouns(inputText);
            Map<String, List<String>> nounsAndExamples = new HashMap<>();
            for (String noun : nouns) {
                List<String> examples = openAIService.getExamples(noun);
                nounsAndExamples.put(noun, examples);
            }
            responseData.put("nounsAndExamples", nounsAndExamples);
            return ApiResponse.success(SuccessMessage.OPENAI_SUCCESS, responseData);
        } catch (Exception e) {
            return ApiResponse.error(ErrorMessage.OPENAI_FAILURE);
        }
    }

}
