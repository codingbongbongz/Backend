package com.swm.cbz.client;


import static org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED_VALUE;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.swm.cbz.dto.apple.TokenResponse;
import java.io.IOException;
import java.io.InputStreamReader;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;

@Component
@RequiredArgsConstructor
public class AppleWebClient {

    private static final String APPLE_TOKEN_VERIFY_URL = "https://appleid.apple.com/auth/token";

    @Value("${apple.key}")
    private String appleConfigPath;
    private String clientId;
    private String redirectUri;

    @SneakyThrows
    public ResponseEntity<TokenResponse> verifyAppleToken(String code, String clientSecret) {

        setKey();
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();

        formData.add("client_id", clientId);
        formData.add("client_secret", clientSecret);
        formData.add("code", code);
        formData.add("grant_type", "authorization_code");
        formData.add("refresh_token", "refreshToken");
        formData.add("redirect_uri", redirectUri);

        WebClient webClient = WebClient.builder()
            .baseUrl(APPLE_TOKEN_VERIFY_URL)
            .defaultHeader(APPLICATION_FORM_URLENCODED_VALUE)
            .build();

        return webClient.post()
            .bodyValue(formData)
            .exchangeToMono(clientResponse -> clientResponse.toEntity(TokenResponse.class))
            .block();
    }

    public void setKey() throws IOException {
        Gson gson = new Gson();
        ClassPathResource resource = new ClassPathResource(appleConfigPath);

        JsonObject object =
            gson.fromJson(new InputStreamReader(resource.getInputStream()), JsonObject.class);
        clientId = String.valueOf(object.get("client_id")).replaceAll("\"", "");
        redirectUri = String.valueOf(object.get("redirect_uri")).replaceAll("\"", "");
    }

}
