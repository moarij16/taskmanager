package com.example.taskmanager.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
public class HomeController {
    @RequestMapping("/")
    public String home() {
        System.out.println("Application Name: " + appName);
        String viewName = getHomePage();
        return viewName;
    }


    private String getHomePage() {
        return "index.html";
    }

    @Value("${spring.application.name}")
    private String appName;
    }
