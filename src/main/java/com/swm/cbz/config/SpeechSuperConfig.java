package com.swm.cbz.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@ConfigurationProperties(prefix = "app")
@Getter
@Setter
public class SpeechSuperConfig {
    private String key;
    private String secret;
}
