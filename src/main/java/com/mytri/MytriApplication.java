package com.mytri;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class MytriApplication {

    private static Logger logger = LogManager.getLogger(MytriApplication.class);

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    public static void main(String[] args) {
        logger.info("Starting Spring Boot application..");
        SpringApplication.run(MytriApplication.class, args);
    }
}
