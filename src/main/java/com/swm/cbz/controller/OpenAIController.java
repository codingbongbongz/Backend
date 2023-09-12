package com.swm.cbz.controller;

import com.swm.cbz.common.response.ApiResponse;
import com.swm.cbz.common.response.ErrorMessage;
import com.swm.cbz.common.response.SuccessMessage;
import com.swm.cbz.service.OpenAIService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class OpenAIController {


    private final OpenAIService openAIService;

    public OpenAIController(OpenAIService openAIService) {
        this.openAIService = openAIService;
    }

    @PostMapping(value = "/fetchGeneratedText", consumes = "text/plain", produces = "application/json")
    public ApiResponse<Map<String, Object>> fetchGeneratedText(@RequestBody String inputText) {
        Map<String, Object> responseData = new HashMap<>();
        try {
            String generatedText = openAIService.fetchOpenAIResponse(inputText);
            responseData.put("generated_text", generatedText);
            return ApiResponse.success(SuccessMessage.OPENAI_SUCCESS, responseData);
        } catch (Exception e) {
            return ApiResponse.error(ErrorMessage.OPENAI_FAILURE);
        }
    }
}
