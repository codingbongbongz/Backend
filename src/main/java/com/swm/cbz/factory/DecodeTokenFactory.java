package com.swm.cbz.factory;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import org.springframework.boot.json.BasicJsonParser;

public class DecodeTokenFactory {

    public static <T> T decodePayload(String token, Class<T> targetClass) {

        final String payloadJWT = token.split("\\.")[1];

        final String payload = new String(java.util.Base64.getUrlDecoder().decode(payloadJWT));

        ObjectMapper objectMapper = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        try {
            return objectMapper.readValue(payload, targetClass);
        } catch (Exception e) {
            throw new RuntimeException("Error decoding token payload", e);
        }
    }

    public static Map<String, Object> decodeToken(String jwtToken) {
        final String payloadJWT = jwtToken.split("\\.")[1];

        final String payload = new String(java.util.Base64.getUrlDecoder().decode(payloadJWT));
        BasicJsonParser jsonParser = new BasicJsonParser();
        Map<String, Object> jsonArray = jsonParser.parseMap(payload);

        return jsonArray;
    }

}
