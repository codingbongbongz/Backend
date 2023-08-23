package com.swm.cbz;

import com.swm.cbz.config.AWSConfig;
import com.swm.cbz.config.SpeechSuperConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;


@EnableJpaAuditing
@SpringBootApplication
@EnableConfigurationProperties({SpeechSuperConfig.class, AWSConfig.class})
public class CbzApplication {

	public static void main(String[] args) {
		SpringApplication.run(CbzApplication.class, args);
	}

}
