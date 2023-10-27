package com.swm.cbz.client;


import static org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED_VALUE;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.swm.cbz.dto.apple.TokenResponse;
import com.swm.cbz.dto.google.response.GoogleTokenClientResponse;
import com.swm.cbz.dto.google.response.GoogleUserInfoResponse;
import java.io.IOException;
import java.io.InputStreamReader;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;

@Component
@RequiredArgsConstructor
public class GoogleWebClient {

    @Value("${google.key}")
    private String googleConfigPath;
    private String clientId;
    private String redirectUri;
    private String clientSecret;
    private String tokenUri;
    private final String googleUserInfoUri = "https://www.googleapis.com/oauth2/v2/userinfo";

    @SneakyThrows
    public ResponseEntity<GoogleTokenClientResponse> getGoogleToken(String code) {
        setKey();
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();

        formData.add("client_id", clientId);
        formData.add("client_secret", clientSecret);
        formData.add("code", code);
        formData.add("grant_type", "refresh_token");
        formData.add("redirect_uri", redirectUri);

        WebClient webClient = WebClient.builder()
            .baseUrl(tokenUri)
            .defaultHeader(APPLICATION_FORM_URLENCODED_VALUE)
            .build();

        return webClient.post()
            .bodyValue(formData)
            .exchangeToMono(clientResponse -> clientResponse.toEntity(GoogleTokenClientResponse.class))
            .block();
    }

    @SneakyThrows
    public ResponseEntity<GoogleUserInfoResponse> getGoogleUserInfo(String accessToken) {
        WebClient webClient = WebClient.builder()
            .baseUrl(googleUserInfoUri)
            .defaultHeader(HttpHeaders.AUTHORIZATION,"Bearer "+ accessToken)
            .build();

        return webClient.get()
            .exchangeToMono(clientResponse -> clientResponse.toEntity(GoogleUserInfoResponse.class))
            .block();
    }

    public void setKey() throws IOException {
        Gson gson = new Gson();
        ClassPathResource resource = new ClassPathResource(googleConfigPath);

        JsonObject object =
            gson.fromJson(new InputStreamReader(resource.getInputStream()), JsonObject.class);
        clientId = String.valueOf(object.get("client_id")).replaceAll("\"", "");
        redirectUri = String.valueOf(object.get("redirect_uri")).replaceAll("\"", "");
        clientSecret = String.valueOf(object.get("google_secret")).replaceAll("\"", "");
        tokenUri = String.valueOf(object.get("token_uri")).replaceAll("\"", "");
    }
}
