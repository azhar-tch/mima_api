package com.helpysoft.mima_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
@EnableScheduling
@SpringBootApplication
public class MimaApplication {
    public static void main(String[] args) {
        SpringApplication.run(MimaApplication.class, args);
    }

}
