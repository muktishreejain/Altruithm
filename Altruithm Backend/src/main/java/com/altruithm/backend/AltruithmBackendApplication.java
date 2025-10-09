package com.altruithm.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.altruithm")
public class AltruithmBackendApplication {
    public static void main(String[] args) {
        SpringApplication.run(AltruithmBackendApplication.class, args);
    }
}