package com.fileshare.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("/test/open")
    public String openEndpoint() {
        return "Open endpoint is working!";
    }
}
