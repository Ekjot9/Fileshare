package com.fileshare.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @GetMapping("/dashboard")
   @PreAuthorize("hasAuthority('ROLE_ADMIN')")

    public ResponseEntity<String> adminDashboard() {
        return ResponseEntity.ok("Welcome to the admin dashboard");
    }
}
