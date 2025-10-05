
package com.fileshare.service;

import com.fileshare.model.User;
import com.fileshare.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired private UserRepository userRepository;
    @Autowired private PasswordEncoder passwordEncoder;

    public boolean isUsernameTaken(String username) {
        return userRepository.existsByUsername(username);
    }

    public String registerUser(User user) {
        if (userRepository.existsByUsername(user.getUsername())) {
            return "Username already taken";
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        if (user.getRole() == null || user.getRole().isBlank()) {
            user.setRole("USER");
        }

        userRepository.save(user);
        return "User registered successfully";
    }
}

