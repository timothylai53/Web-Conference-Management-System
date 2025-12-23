package com.webcrafters.confease_backend.controller;

import com.webcrafters.confease_backend.model.User;
import com.webcrafters.confease_backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> loginRequest) {
        String email = loginRequest.get("email");
        String password = loginRequest.get("password");

        // Find user by email
        Optional<User> userOpt = userRepository.findByEmail(email);

        if (userOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "Invalid email or password"));
        }

        User user = userOpt.get();

        // Check password (note: in production, you should use password hashing)
        if (!user.getPassword_hash().equals(password)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "Invalid email or password"));
        }

        // Return user info (without password)
        Map<String, Object> response = new HashMap<>();
        response.put("user_id", user.getUser_id());
        response.put("email", user.getEmail());
        response.put("first_name", user.getFirst_name());
        response.put("last_name", user.getLast_name());
        response.put("affiliation", user.getAffiliation());
        response.put("country", user.getCountry());
        response.put("role", user.getRole());
        response.put("category", user.getCategory());
        response.put("orcid", user.getOrcid());

        return ResponseEntity.ok(response);
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        // Check if email already exists
        Optional<User> existingUser = userRepository.findByEmail(user.getEmail());
        if (existingUser.isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", "Email already registered"));
        }

        // Set default values
        if (user.getRole() == null || user.getRole().isEmpty()) {
            user.setRole("User");
        }
        if (user.getIs_email_verified() == null) {
            user.setIs_email_verified(false);
        }

        // Save user
        User savedUser = userRepository.save(user);

        // Return user info (without password)
        Map<String, Object> response = new HashMap<>();
        response.put("user_id", savedUser.getUser_id());
        response.put("email", savedUser.getEmail());
        response.put("first_name", savedUser.getFirst_name());
        response.put("last_name", savedUser.getLast_name());
        response.put("role", savedUser.getRole());

        return ResponseEntity.ok(response);
    }
}
