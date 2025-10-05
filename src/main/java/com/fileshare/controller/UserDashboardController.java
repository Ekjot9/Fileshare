package com.fileshare.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserDashboardController {

    @GetMapping("/dashboard")
    @PreAuthorize("hasRole('USER')")   // ✅ Only 'USER' role can access
    public ResponseEntity<String> userDashboard() {
        return ResponseEntity.ok("Welcome to the user dashboard");
    }
}
