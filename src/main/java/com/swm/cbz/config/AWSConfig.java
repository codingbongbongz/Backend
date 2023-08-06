package com.swm.cbz.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@ConfigurationProperties(prefix = "aws")
@Getter
@Setter

public class AWSConfig {
    private String accessKey;
    private String secretKey;
    private String region;
    private String bucketName;

}
