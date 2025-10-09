package com.altruithm.backend.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @GetMapping("/")
    public String home() {
        return "Welcome to Charity Management API! Visit /api/charities to see all charities.";
    }

    @GetMapping("/api")
    public String apiInfo() {
        return "Charity Management API v1.0 - Available endpoints: /api/charities";
    }
}