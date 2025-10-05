package com.fileshare.controller;

import com.fileshare.model.User;
import com.fileshare.repository.UserRepository;
import com.fileshare.security.JwtUtil;
import com.fileshare.security.CustomUserDetailsService;
import com.fileshare.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*")
public class UserController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    // ✅ REGISTER endpoint
    @PostMapping("/register")
public ResponseEntity<?> register(@RequestBody User user) {
    if (userRepository.existsByUsername(user.getUsername())) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(Map.of("error", "Username already taken"));
    }

    try {
        userService.registerUser(user);
        return ResponseEntity.ok(Map.of("message", "User registered successfully"));
    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Registration failed"));
    }
}


    // ✅ LOGIN endpoint (used by frontend)
   

   @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<?> getAllUsers() {
        List<User> users = userRepository.findAll();
        List<Map<String, String>> result = users.stream().map(user -> {
            Map<String, String> map = new HashMap<>();
            map.put("username", user.getUsername());
            map.put("role", user.getRole());
            return map;
        }).collect(Collectors.toList());

        return ResponseEntity.ok(result);
    }  
}