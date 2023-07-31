package com.swm.cbz;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class CbzApplication {

	public static void main(String[] args) {
		SpringApplication.run(CbzApplication.class, args);
	}

}
